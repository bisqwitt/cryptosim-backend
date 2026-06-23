package ch.santis.cryptosim.backend.mapper;

import ch.santis.cryptosim.backend.dto.transaction.CreateTransactionRequest;
import ch.santis.cryptosim.backend.dto.transaction.TransactionResponse;
import ch.santis.cryptosim.backend.entity.Portfolio;
import ch.santis.cryptosim.backend.entity.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionMapper {

    public Transaction fromRequest(CreateTransactionRequest request, Portfolio portfolio, BigDecimal amountCrypto) {
        return new Transaction(
            portfolio,
                request.cryptoId(),
                request.amountUsd(),
                amountCrypto,
                request.type(),
                request.date()
        );
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getPortfolio().getId(),
                transaction.getCryptoId(),
                transaction.getAmountUsd(),
                transaction.getAmountCrypto(),
                transaction.getType(),
                transaction.getDate()
        );
    }

}
