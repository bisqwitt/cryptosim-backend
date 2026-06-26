package ch.santis.cryptosim.backend.dto.portfolio;

import ch.santis.cryptosim.backend.dto.transaction.TransactionResponse;

import java.util.List;

public record PortfolioPosition(
        String cryptoId,
        String name,
        String symbol,
        PortfolioPositionHoldingResponse holding,
        List<TransactionResponse> transactions
) {
}
