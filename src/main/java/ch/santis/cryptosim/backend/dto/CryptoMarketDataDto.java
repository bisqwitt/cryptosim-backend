package ch.santis.cryptosim.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CryptoMarketDataDto(
   @JsonProperty("id")
   String id,

   @JsonProperty("name")
   String name,

    @JsonProperty("symbol")
   String symbol,

   @JsonProperty("current_price")
   BigDecimal currentPrice,

   @JsonProperty("price_change_percentage_24h")
   BigDecimal priceChangePercentage24h,

   @JsonProperty("price_change_percentage_7d_in_currency")
   BigDecimal priceChangePercentage7d,

   @JsonProperty("market_cap")
   BigDecimal marketCap,

   @JsonProperty("total_volume")
   BigDecimal totalVolume
) {}
