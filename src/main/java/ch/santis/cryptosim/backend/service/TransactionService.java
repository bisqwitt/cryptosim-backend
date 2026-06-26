package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.dto.portfolio.PortfolioPositionHoldingResponse;
import ch.santis.cryptosim.backend.dto.transaction.CreateTransactionRequest;
import ch.santis.cryptosim.backend.dto.transaction.TransactionResponse;
import ch.santis.cryptosim.backend.entity.Portfolio;
import ch.santis.cryptosim.backend.entity.Transaction;
import ch.santis.cryptosim.backend.entity.TransactionType;
import ch.santis.cryptosim.backend.error.CoinGeckoApiException;
import ch.santis.cryptosim.backend.error.ValidationException;
import ch.santis.cryptosim.backend.mapper.TransactionMapper;
import ch.santis.cryptosim.backend.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final CryptoService cryptoService;
    private final PortfolioService portfolioService;

    private final TransactionMapper transactionMapper;

    public TransactionService(
            TransactionRepository transactionRepository,
            CryptoService cryptoService,
            PortfolioService portfolioService,
            TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.cryptoService = cryptoService;
        this.portfolioService = portfolioService;
        this.transactionMapper = transactionMapper;
    }

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        validateCreateTransactionRequest(request);

        Portfolio portfolio = portfolioService.get(request.portfolioId());
        BigDecimal cryptoAmount = request.amountUsd()
                .divide(cryptoService.getPrice(request.cryptoId(), request.date()).price(),
                        18, RoundingMode.HALF_UP);

        Transaction saved = transactionRepository.save(transactionMapper.fromRequest(request, portfolio, cryptoAmount));
        portfolioService.adjustCredit(request.portfolioId(), request.amountUsd(), request.type());

        return transactionMapper.toResponse(saved);
    }

    private void validateCreateTransactionRequest(CreateTransactionRequest request) {
        validatePortfolioId(request.portfolioId());
        validateCryptoId(request.cryptoId());
        validateTransactionType(request.type());
        validateDate(request.date());
        validateAmount(request);
    }

    private void validatePortfolioId(Long id) {
        portfolioService.get(id);
    }

    private void validateCryptoId(String id) {
        if(id == null || id.isBlank()) {
            throw CoinGeckoApiException.from(HttpStatus.NOT_FOUND);
        }
    }

    private void validateTransactionType(TransactionType type) {
        if(type == null) throw new ValidationException(
                "Missing transaction type.",
                "Please select a transaction type."
        );
    }

    private void validateDate(LocalDate date) {
        if(date == null) throw new ValidationException(
                "Missing date.",
                "Please select a date."
        );

        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Date is invalid.",
                    "The date cannot be in the future."
            );
        }

        if (date.isBefore(LocalDate.now().minusDays(365))) {
            throw new ValidationException(
                    "Date is invalid.",
                    "The Date must be within the last 365 days."
            );
        }
    }

    private void validateAmount(CreateTransactionRequest request) {
        Portfolio portfolio = portfolioService.get(request.portfolioId());
        if(request.type() == TransactionType.BUY
                && portfolio.getCredit()
                    .compareTo(request.amountUsd()) < 0) {
            throw new ValidationException(
                    "Invalid amount usd",
                    "Not enough credit"
            );
        } else if(request.type() == TransactionType.SELL) {
            PortfolioPositionHoldingResponse holdings = portfolioService.getHoldingOfPosition(
                    portfolio.getId(),
                    request.cryptoId(),
                    request.date());

            if(holdings.value().compareTo(request.amountUsd()) < 0) {
                throw new ValidationException(
                        "Invalid amount usd",
                        "Not enough holdings"
                );
            }
        }
    }

}
