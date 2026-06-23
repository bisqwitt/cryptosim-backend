package ch.santis.cryptosim.backend.dto.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CoinGeckoPriceAtDateResponse(

        @JsonProperty("id")
        String id,

        @JsonProperty("market_data")
        MarketData marketData
) {

    public record MarketData(
            @JsonProperty("current_price")
            UsdValue currentPrice
    ) {}

    public record UsdValue(

            @JsonProperty("usd")
            BigDecimal usd

    ) {}
}