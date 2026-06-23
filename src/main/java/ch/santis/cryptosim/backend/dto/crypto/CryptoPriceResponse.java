package ch.santis.cryptosim.backend.dto.crypto;

import java.math.BigDecimal;

public record CryptoPriceResponse(
        String id,
        BigDecimal price
) {
}
