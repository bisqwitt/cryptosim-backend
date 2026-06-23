package ch.santis.cryptosim.backend.dto.coingecko;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CoinGeckoPriceResponse(
        @JsonProperty("usd")
        BigDecimal usd
) {}
