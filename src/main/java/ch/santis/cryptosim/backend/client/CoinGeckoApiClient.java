package ch.santis.cryptosim.backend.client;

import ch.santis.cryptosim.backend.dto.CryptoMarketDataDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;

@Component
public class CoinGeckoApiClient {

    private final RestClient restClient;

    public CoinGeckoApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<CryptoMarketDataDto> getMarketData() {
        return restClient.get()
                .uri(this::buildMarketDataUri)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    private URI buildMarketDataUri(UriBuilder builder) {
        return builder
                .path("/coins/markets")
                .queryParam("vs_currency", "usd")
                .queryParam("order", "market_cap_desc")
                .queryParam("per_page", 150)
                .queryParam("price_change_percentage", "24h,7d")
                .build();
    }

}
