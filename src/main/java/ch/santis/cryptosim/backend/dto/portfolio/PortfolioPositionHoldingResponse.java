package ch.santis.cryptosim.backend.dto.portfolio;

import java.math.BigDecimal;

public record PortfolioPositionHoldingResponse(
        BigDecimal holding,
        BigDecimal value
) {
}
