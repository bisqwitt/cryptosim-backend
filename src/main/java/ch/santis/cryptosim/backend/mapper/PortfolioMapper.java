package ch.santis.cryptosim.backend.mapper;

import ch.santis.cryptosim.backend.dto.portfolio.CreatePortfolioRequest;
import ch.santis.cryptosim.backend.dto.portfolio.PortfolioResponse;
import ch.santis.cryptosim.backend.entity.Portfolio;
import org.springframework.stereotype.Component;

@Component
public class PortfolioMapper {

    public Portfolio fromRequest(CreatePortfolioRequest request) {
        return new Portfolio(request.name(), request.credit());
    }

    public PortfolioResponse toResponse(Portfolio portfolio) {
        return new PortfolioResponse(
                portfolio.getId(),
                portfolio.getName(),
                portfolio.getCredit()
        );
    }

}
