package ch.santis.cryptosim.backend.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CryptoSimException.class)
    public ResponseEntity<ErrorResponse> handleCoinGeckoApiException(CryptoSimException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getStatus().value(), e.getMessage(), e.getSuggestion()));
    }
}
