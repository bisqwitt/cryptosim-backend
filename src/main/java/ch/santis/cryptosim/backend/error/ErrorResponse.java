package ch.santis.cryptosim.backend.error;

public record ErrorResponse(
        int status,
        String message,
        String suggestion
) {}
