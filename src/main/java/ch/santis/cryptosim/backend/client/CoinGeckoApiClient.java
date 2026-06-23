package ch.santis.cryptosim.backend.client;

import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketChartResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoPriceAtDateResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoPriceResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class CoinGeckoApiClient {

    private final RestClient restClient;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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

    public Map<String, CoinGeckoPriceResponse> getCoinPrice(String id) {
        return restClient.get()
                .uri(builder -> buildCoinPriceUri(builder, id))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public CoinGeckoPriceAtDateResponse getCoinPriceAtDate(String id, LocalDate date) {
        return restClient.get()
                .uri(builder -> buildCoinPriceAtDateUri(builder, id, date.format(dateFormatter)))
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

    private URI buildCoinPriceUri(UriBuilder builder, String id) {
        return builder
                .path("/simple/price")
                .queryParam("ids", id)
                .queryParam("vs_currencies", "usd")
                .queryParam("include_market_cap", false)
                .queryParam("include_24hr_vol", false)
                .queryParam("include_24hr_change", false)
                .queryParam("include_last_updated_at", false)
                .build();
    }

    private URI buildCoinPriceAtDateUri(UriBuilder builder, String id, String date) {
        return builder
                .path("/coins/{id}/history")
                .queryParam("date", date)
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
