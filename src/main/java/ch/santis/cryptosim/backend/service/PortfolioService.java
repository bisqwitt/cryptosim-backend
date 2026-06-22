package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.controller.PortfolioController;
import ch.santis.cryptosim.backend.dto.portfolio.CreatePortfolioRequest;
import ch.santis.cryptosim.backend.dto.portfolio.PortfolioResponse;
import ch.santis.cryptosim.backend.entity.Portfolio;
import ch.santis.cryptosim.backend.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public List<PortfolioResponse> getAll() {
        return portfolioRepository.findAll().stream()
                .map(portfolio -> new PortfolioResponse(
                        portfolio.getId(),
                        portfolio.getName(),
                        portfolio.getCredit()
                ))
                .toList();
    }

    public PortfolioResponse createPortfolio(CreatePortfolioRequest request) {
        Portfolio portfolio = new Portfolio();
        portfolio.setName(request.name());
        portfolio.setCredit(request.credit());

        Portfolio saved = portfolioRepository.save(portfolio);
        return new PortfolioResponse(
                saved.getId(),
                saved.getName(),
                saved.getCredit()
        );
    }

}
