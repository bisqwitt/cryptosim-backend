package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.dto.transaction.CreateTransactionRequest;
import ch.santis.cryptosim.backend.dto.transaction.TransactionResponse;
import ch.santis.cryptosim.backend.entity.Portfolio;
import ch.santis.cryptosim.backend.entity.Transaction;
import ch.santis.cryptosim.backend.mapper.TransactionMapper;
import ch.santis.cryptosim.backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        Portfolio portfolio = portfolioService.get(request.portfolioId());
        BigDecimal cryptoAmount = request.amountUsd()
                .divide(cryptoService.getPrice(request.cryptoId(), request.date()).price(),
                        18, RoundingMode.HALF_UP);

        Transaction saved = transactionRepository.save(transactionMapper.fromRequest(request, portfolio, cryptoAmount));
        portfolioService.adjustCredit(request.portfolioId(), request.amountUsd(), request.type());

        return transactionMapper.toResponse(saved);
    }

}
