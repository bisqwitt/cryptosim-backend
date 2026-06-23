package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.dto.transaction.CreateTransactionRequest;
import ch.santis.cryptosim.backend.dto.transaction.TransactionResponse;
import ch.santis.cryptosim.backend.entity.Transaction;
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

    public TransactionService(TransactionRepository transactionRepository, CryptoService cryptoService, PortfolioService portfolioService) {
        this.transactionRepository = transactionRepository;
        this.cryptoService = cryptoService;
        this.portfolioService = portfolioService;
    }

    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        BigDecimal cryptoAmount = request.amountUsd()
                .divide(cryptoService.getPrice(request.cryptoId(), request.date()).price(),
                        18, RoundingMode.HALF_UP);

        Transaction transaction = new Transaction();
        transaction.setPortfolio(portfolioService.get(request.portfolioId()));
        transaction.setCryptoId(request.cryptoId());
        transaction.setAmountUsd(request.amountUsd());
        transaction.setAmountCrypto(cryptoAmount);
        transaction.setType(request.type());
        transaction.setDate(request.date());

        Transaction saved = transactionRepository.save(transaction);
        return new TransactionResponse(
                saved.getId(),
                saved.getPortfolio().getId(),
                saved.getCryptoId(),
                saved.getAmountUsd(),
                saved.getAmountCrypto(),
                saved.getType(),
                saved.getDate()
        );
    }

}
