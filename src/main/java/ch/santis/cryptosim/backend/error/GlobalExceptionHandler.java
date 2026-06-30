package ch.santis.cryptosim.backend.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CryptoSimException.class)
    public ResponseEntity<ErrorResponse> handleCryptoSimException(CryptoSimException e) {
        log.warn("CryptoSimException: {} | Suggestion: {}",
                e.getMessage(),
                e.getSuggestion(),
                e);

        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(
                        e.getStatus().value(),
                        e.getMessage(),
                        e.getSuggestion()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException", e);

        return ResponseEntity
                .status(400)
                .body(new ErrorResponse(
                        400,
                        "The data could not be saved.",
                        "Please check the entered values."
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected exception", e);

        return ResponseEntity
                .status(500)
                .body(new ErrorResponse(
                        500,
                        "An unexpected error occurred.",
                        "Please contact the administrator."
                ));
    }
}
