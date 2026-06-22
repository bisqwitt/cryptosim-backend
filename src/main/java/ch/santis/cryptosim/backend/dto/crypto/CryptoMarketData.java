package ch.santis.cryptosim.backend.dto.crypto;

import java.math.BigDecimal;

public record CryptoMarketData(
   String id,
   String name,
   String symbol,
   BigDecimal currentPrice,
   BigDecimal priceChangePercentage24h,
   BigDecimal priceChangePercentage7d,
   BigDecimal marketCap,
   BigDecimal totalVolume
) {}
