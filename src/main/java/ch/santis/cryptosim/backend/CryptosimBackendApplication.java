package ch.santis.cryptosim.backend;

import ch.santis.cryptosim.backend.client.CoinGeckoApiClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CryptosimBackendApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CryptosimBackendApplication.class, args);
		CoinGeckoApiClient client = context.getBean(CoinGeckoApiClient.class);
		System.out.println(client.getCoins());
	}

}
