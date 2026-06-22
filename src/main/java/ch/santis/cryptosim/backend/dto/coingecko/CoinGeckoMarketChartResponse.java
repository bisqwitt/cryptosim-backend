package ch.santis.cryptosim.backend.dto.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CoinGeckoMarketChartResponse(
        @JsonProperty("prices")
        List<CoinGeckoMarketChartPoint> prices,

        @JsonProperty("market_caps")
        List<CoinGeckoMarketChartPoint> marketCaps,

        @JsonProperty("total_volumes")
        List<CoinGeckoMarketChartPoint> totalVolumes
) {
}
