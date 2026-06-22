package ch.santis.cryptosim.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CryptoMarketDataDto(
   String id,
   String name,
   String symbol,
   BigDecimal currentPrice,
   BigDecimal priceChangePercentage24h,
   BigDecimal priceChangePercentage7d,
   BigDecimal marketCap,
   BigDecimal totalVolume
) {}
