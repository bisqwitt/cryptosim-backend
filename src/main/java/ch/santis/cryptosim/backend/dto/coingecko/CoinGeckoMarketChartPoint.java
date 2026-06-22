package ch.santis.cryptosim.backend.dto.coingecko;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public record CoinGeckoMarketChartPoint(
        long timestamp,
        BigDecimal value
) {
}