package ch.santis.cryptosim.backend.dto.crypto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CryptoHistoricalDataResponse(
        @JsonProperty("prices")
        List<CryptoHistoricalDataPoint> prices,

        @JsonProperty("market_caps")
        List<CryptoHistoricalDataPoint> marketCaps,

        @JsonProperty("total_volumes")
        List<CryptoHistoricalDataPoint> totalVolumes
) {
}
