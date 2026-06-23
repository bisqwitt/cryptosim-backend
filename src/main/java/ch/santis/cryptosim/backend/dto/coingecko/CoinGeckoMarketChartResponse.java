package ch.santis.cryptosim.backend.dto.coingecko;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record CoinGeckoMarketChartResponse(

        @JsonProperty("prices")
        List<MarketChartPoint> prices,

        @JsonProperty("market_caps")
        List<MarketChartPoint> marketCaps,

        @JsonProperty("total_volumes")
        List<MarketChartPoint> totalVolumes
) {
        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        public record MarketChartPoint(
                long timestamp,
                BigDecimal value
        ) {}
}