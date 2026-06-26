package ch.santis.cryptosim.backend.dto.portfolio;

import java.util.List;

public record PortfolioDetailsResponse(
        PortfolioResponse portfolio,
        List<PortfolioPosition> positions
) {
}
