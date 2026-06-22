package ch.santis.cryptosim.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public record CryptoHistoricalDataPointDto(
        long timestamp,
        BigDecimal value
) {
}
