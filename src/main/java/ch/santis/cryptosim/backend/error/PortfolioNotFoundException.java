package ch.santis.cryptosim.backend.error;

import org.springframework.http.HttpStatusCode;

public class PortfolioNotFoundException extends CryptoSimException {
    public PortfolioNotFoundException(HttpStatusCode status, String message, String suggestion) {
        super(status, message, suggestion);
    }
}
