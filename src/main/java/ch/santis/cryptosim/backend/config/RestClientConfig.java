package ch.santis.cryptosim.backend.config;

import ch.santis.cryptosim.backend.error.CoinGeckoApiException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    RestClient coinGeckoRestClient(RestClient.Builder builder, CoinGeckoApiProperties properties) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("x-cg-demo-api-key", properties.getKey())
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        (request, response) -> {
                            throw CoinGeckoApiException.from(response.getStatusCode());
                        }
                )
                .build();
    }
}
