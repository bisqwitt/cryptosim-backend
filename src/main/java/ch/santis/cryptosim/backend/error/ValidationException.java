package ch.santis.cryptosim.backend.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ValidationException extends CryptoSimException {
    public ValidationException(String message, String suggestion) {
        super(HttpStatus.BAD_REQUEST, message, suggestion);
    }
}
