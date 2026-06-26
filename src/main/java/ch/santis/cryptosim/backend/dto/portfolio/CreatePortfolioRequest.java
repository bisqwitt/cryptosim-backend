package ch.santis.cryptosim.backend.dto.portfolio;

import java.math.BigDecimal;

public record CreatePortfolioRequest(
        String name,
        BigDecimal credit
) {

        public CreatePortfolioRequest {
                if(name != null) name = name.trim();
        }

}
