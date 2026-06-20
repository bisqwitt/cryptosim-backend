package ch.santis.cryptosim.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CryptosimBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptosimBackendApplication.class, args);
	}

}
