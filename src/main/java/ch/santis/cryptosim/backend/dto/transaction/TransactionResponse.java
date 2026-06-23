package ch.santis.cryptosim.backend.dto.transaction;

import ch.santis.cryptosim.backend.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long portfolioId,
        String cryptoId,
        BigDecimal amountUsd,
        BigDecimal amountCrypto,
        TransactionType type,
        LocalDate date
) {
}
