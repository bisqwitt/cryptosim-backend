package ch.santis.cryptosim.backend.error;

import org.springframework.http.HttpStatusCode;

public class CryptoSimException extends RuntimeException {

    private final HttpStatusCode status;
    private final String suggestion;

    protected CryptoSimException(HttpStatusCode status, String message, String suggestion) {
        super(message);
        this.status = status;
        this.suggestion = suggestion;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public String getSuggestion() {
        return suggestion;
    }

}
