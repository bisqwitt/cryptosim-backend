package ch.santis.cryptosim.backend.client;

import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketChartResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketResponse;
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

    public List<CoinGeckoMarketResponse> getMarkets() {
        return restClient.get()
                .uri(this::buildMarketsUri)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public CoinGeckoMarketResponse getCoin(String id) {
        return restClient.get()
                .uri(builder -> buildCoinUri(builder, id))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public CoinGeckoMarketChartResponse getMarketChart(String id) {
        return restClient.get()
                .uri(builder -> buildMarketChartUri(builder, id))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    private URI buildMarketsUri(UriBuilder builder) {
        return builder
                .path("/coins/markets")
                .queryParam("vs_currency", "usd")
                .queryParam("order", "market_cap_desc")
                .queryParam("per_page", 150)
                .queryParam("price_change_percentage", "24h,7d")
                .build();
    }

    private URI buildCoinUri(UriBuilder builder, String id) {
        return builder
                .path("/coins/{id}")
                .build(id);
    }

    private URI buildMarketChartUri(UriBuilder builder, String id) {
        return builder
                .path("/coins/{id}/market_chart")
                .queryParam("vs_currency", "usd")
                .queryParam("days", 365)
                .build(id);
    }

}
