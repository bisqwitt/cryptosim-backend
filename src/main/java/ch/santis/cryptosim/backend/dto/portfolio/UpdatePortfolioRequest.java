package ch.santis.cryptosim.backend.dto.portfolio;

import org.hibernate.sql.Update;

public record UpdatePortfolioRequest(
        String newName
) {

    public UpdatePortfolioRequest {
        if(newName != null) newName = newName.trim();
    }

}
