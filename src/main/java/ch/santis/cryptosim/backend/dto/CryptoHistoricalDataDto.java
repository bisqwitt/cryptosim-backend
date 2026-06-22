package ch.santis.cryptosim.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CryptoHistoricalDataDto(
        @JsonProperty("prices")
        List<CryptoHistoricalDataPointDto> prices,

        @JsonProperty("market_caps")
        List<CryptoHistoricalDataPointDto> marketCaps,

        @JsonProperty("total_volumes")
        List<CryptoHistoricalDataPointDto> totalVolumes
) {
}
