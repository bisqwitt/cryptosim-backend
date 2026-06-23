package ch.santis.cryptosim.backend.dto.transaction;

import ch.santis.cryptosim.backend.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransactionRequest(
        Long portfolioId,
        String cryptoId,
        BigDecimal amountUsd,
        TransactionType type,
        LocalDate date
) {
}
