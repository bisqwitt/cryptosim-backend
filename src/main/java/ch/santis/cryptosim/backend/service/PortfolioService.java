package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.controller.PortfolioController;
import ch.santis.cryptosim.backend.dto.portfolio.CreatePortfolioRequest;
import ch.santis.cryptosim.backend.dto.portfolio.PortfolioPositionHoldingResponse;
import ch.santis.cryptosim.backend.dto.portfolio.PortfolioResponse;
import ch.santis.cryptosim.backend.entity.Portfolio;
import ch.santis.cryptosim.backend.entity.Transaction;
import ch.santis.cryptosim.backend.entity.TransactionType;
import ch.santis.cryptosim.backend.error.PortfolioNotFoundException;
import ch.santis.cryptosim.backend.repository.PortfolioRepository;
import ch.santis.cryptosim.backend.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;

    private final CryptoService cryptoService;

    public PortfolioService(PortfolioRepository portfolioRepository, TransactionRepository transactionRepository, CryptoService cryptoService) {
        this.portfolioRepository = portfolioRepository;
        this.transactionRepository = transactionRepository;
        this.cryptoService = cryptoService;
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

    public PortfolioPositionHoldingResponse getHoldingOfPositionAtDate(Long id, String cryptoId, LocalDate date) {
        BigDecimal holding = transactionRepository.findByPortfolioIdAndCryptoId(id, cryptoId)
                .stream()
                .filter(transaction -> !transaction.getDate().isAfter(date))
                .map(transaction -> transaction.getType() == TransactionType.BUY
                        ? transaction.getAmountCrypto()
                        : transaction.getAmountCrypto().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal value = cryptoService.getPrice(cryptoId, date).price().multiply(holding);
        return new PortfolioPositionHoldingResponse(holding, value);
    }

    public Portfolio get(Long id) {
        Optional<Portfolio> optionalPortfolio = portfolioRepository.findById(id);
        if(optionalPortfolio.isEmpty()) {
            throw new PortfolioNotFoundException(HttpStatus.NOT_FOUND, "Portfolio not found", "Portfolio with id: " + id + " was not found.");
        }
        return optionalPortfolio.get();
    }

}
