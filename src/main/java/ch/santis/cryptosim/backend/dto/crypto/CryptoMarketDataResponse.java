package ch.santis.cryptosim.backend.dto.crypto;

import java.math.BigDecimal;

public record CryptoMarketDataResponse(
   String id,
   String name,
   String symbol,
   BigDecimal currentPrice,
   BigDecimal priceChangePercentage24h,
   BigDecimal priceChangePercentage7d,
   BigDecimal marketCap,
   BigDecimal totalVolume
) {}
