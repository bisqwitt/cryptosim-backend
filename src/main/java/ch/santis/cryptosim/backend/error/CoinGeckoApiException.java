package ch.santis.cryptosim.backend.error;

import org.springframework.http.HttpStatusCode;

public class CoinGeckoApiException extends CryptoSimException {

    private CoinGeckoApiException(HttpStatusCode status, String message, String suggestion) {
        super(status, message, suggestion);
    }

    public static CoinGeckoApiException from(HttpStatusCode status) {
        String message = switch (status.value()) {
            case 400 -> "Invalid request parameters.";
            case 401 -> "CoinGecko API key is invalid or missing.";
            case 403 -> "Access to CoinGecko was denied.";
            case 429 -> "CoinGecko rate limit reached.";
            case 500, 503 -> "CoinGecko API is currently unavailable.";
            default -> "CoinGecko request failed.";
        };

        String suggestion = switch (status.value()) {
            case 400, 403 -> "Please contact the administrator.";
            case 401 -> "Please setup a valid API key.";
            case 429 -> "Please wait a minute before trying again.";
            default -> "";
        };

        return new CoinGeckoApiException(status, message, suggestion);
    }
}
