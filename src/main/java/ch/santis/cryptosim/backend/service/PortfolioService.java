package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.dto.portfolio.CreatePortfolioRequest;
import ch.santis.cryptosim.backend.dto.portfolio.PortfolioPositionHoldingResponse;
import ch.santis.cryptosim.backend.dto.portfolio.PortfolioResponse;
import ch.santis.cryptosim.backend.entity.Portfolio;
import ch.santis.cryptosim.backend.entity.TransactionType;
import ch.santis.cryptosim.backend.error.PortfolioNotFoundException;
import ch.santis.cryptosim.backend.mapper.PortfolioMapper;
import ch.santis.cryptosim.backend.repository.PortfolioRepository;
import ch.santis.cryptosim.backend.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;

    private final CryptoService cryptoService;

    private final PortfolioMapper mapper;

    public PortfolioService(
            PortfolioRepository portfolioRepository,
            TransactionRepository transactionRepository,
            CryptoService cryptoService,
            PortfolioMapper mapper) {
        this.portfolioRepository = portfolioRepository;
        this.transactionRepository = transactionRepository;
        this.cryptoService = cryptoService;
        this.mapper = mapper;
    }

    public List<PortfolioResponse> getAll() {
        return portfolioRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public PortfolioResponse createPortfolio(CreatePortfolioRequest request) {
        Portfolio saved = portfolioRepository.save(mapper.fromRequest(request));
        return mapper.toResponse(saved);
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

    public void adjustCredit(Long id, BigDecimal amount, TransactionType type) {
        Portfolio portfolio = get(id);
        portfolio.setCredit(type == TransactionType.BUY
                ? portfolio.getCredit().subtract(amount)
                : portfolio.getCredit().add(amount));

        portfolioRepository.save(portfolio);
    }

    public Portfolio get(Long id) {
        Optional<Portfolio> optionalPortfolio = portfolioRepository.findById(id);
        if(optionalPortfolio.isEmpty()) {
            throw new PortfolioNotFoundException(HttpStatus.NOT_FOUND, "Portfolio not found", "Portfolio with id: " + id + " was not found.");
        }
        return optionalPortfolio.get();
    }

}
