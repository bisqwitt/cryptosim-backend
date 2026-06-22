package ch.santis.cryptosim.backend.dto.crypto;

import java.math.BigDecimal;

public record CryptoHistoricalDataPoint(
        long timestamp,
        BigDecimal value
) {
}
