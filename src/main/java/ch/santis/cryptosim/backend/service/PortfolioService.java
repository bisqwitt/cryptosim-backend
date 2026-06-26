package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.dto.crypto.CryptoMarketDataResponse;
import ch.santis.cryptosim.backend.dto.portfolio.*;
import ch.santis.cryptosim.backend.dto.transaction.TransactionResponse;
import ch.santis.cryptosim.backend.entity.Portfolio;
import ch.santis.cryptosim.backend.entity.Transaction;
import ch.santis.cryptosim.backend.entity.TransactionType;
import ch.santis.cryptosim.backend.error.PortfolioNotFoundException;
import ch.santis.cryptosim.backend.error.ValidationException;
import ch.santis.cryptosim.backend.mapper.PortfolioMapper;
import ch.santis.cryptosim.backend.mapper.TransactionMapper;
import ch.santis.cryptosim.backend.repository.PortfolioRepository;
import ch.santis.cryptosim.backend.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;

    private final CryptoService cryptoService;

    private final PortfolioMapper portfolioMapper;
    private final TransactionMapper transactionMapper;

    public PortfolioService(
            PortfolioRepository portfolioRepository,
            TransactionRepository transactionRepository,
            CryptoService cryptoService,
            PortfolioMapper portfolioMapper,
            TransactionMapper transactionMapper) {
        this.portfolioRepository = portfolioRepository;
        this.transactionRepository = transactionRepository;
        this.cryptoService = cryptoService;
        this.portfolioMapper = portfolioMapper;
        this.transactionMapper = transactionMapper;
    }

    public List<PortfolioResponse> getAll() {
        return portfolioRepository.findAll().stream()
                .map(portfolioMapper::toResponse)
                .toList();
    }

    public PortfolioResponse createPortfolio(CreatePortfolioRequest request) {
        validateCreatePortfolioRequest(request);
        Portfolio saved = portfolioRepository.save(portfolioMapper.fromRequest(request));
        return portfolioMapper.toResponse(saved);
    }

    public PortfolioResponse updatePortfolio(Long id, UpdatePortfolioRequest request) {
        validateUpdatePortfolioRequest(request);
        Portfolio portfolio = get(id);
        portfolio.setName(request.newName());
        portfolioRepository.save(portfolio);

        return portfolioMapper.toResponse(portfolio);
    }

    @Transactional
    public void deletePortfolio(Long id) {
        Portfolio portfolio = get(id);
        transactionRepository.deleteByPortfolioId(id);
        portfolioRepository.delete(portfolio);
    }

    public PortfolioDetailsResponse getPortfolioDetails(Long id) {
        Portfolio portfolio = get(id);
        List<Transaction> transactions = transactionRepository.findByPortfolioId(id);

        List<PortfolioPosition> positions = transactions
                .stream()
                .collect(Collectors.groupingBy(Transaction::getCryptoId))
                .entrySet()
                .stream()
                .map(entry -> {
                    String cryptoId = entry.getKey();
                    CryptoMarketDataResponse crypto = cryptoService.getMarketData(cryptoId);
                    List<Transaction> cryptoTransactions = entry.getValue();
                    PortfolioPositionHoldingResponse holding = getHoldingOfPosition(id, cryptoId, null);

                    List<TransactionResponse> transactionResponses = cryptoTransactions.stream()
                            .map(transactionMapper::toResponse)
                            .toList();

                    return new PortfolioPosition(
                            cryptoId,
                            crypto.name(),
                            crypto.symbol(),
                            holding,
                            transactionResponses
                    );
                })
                .toList();

        return new PortfolioDetailsResponse(
                new PortfolioResponse(
                        portfolio.getId(),
                        portfolio.getName(),
                        portfolio.getCredit()
                ),
                positions
        );
    }

    public PortfolioPositionHoldingResponse getHoldingOfPosition(Long id, String cryptoId, LocalDate date) {
        Stream<Transaction> transactionsOfCrypto = transactionRepository.findByPortfolioIdAndCryptoId(id, cryptoId)
                .stream()
                .filter(transaction -> date == null || !transaction.getDate().isAfter(date));

        BigDecimal holding = calcHoldingOfPosition(transactionsOfCrypto);
        BigDecimal value = cryptoService.getPrice(cryptoId, date).price().multiply(holding);
        return new PortfolioPositionHoldingResponse(holding, value);
    }

    private BigDecimal calcHoldingOfPosition(Stream<Transaction> transactionsOfCrypto) {
        return transactionsOfCrypto
                .map(transaction -> transaction.getType() == TransactionType.BUY
                        ? transaction.getAmountCrypto()
                        : transaction.getAmountCrypto().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
            throw new ValidationException(
                    "Portfolio not found",
                    "Portfolio with id: " + id + " was not found.");
        }
        return optionalPortfolio.get();
    }

    private void validateCreatePortfolioRequest(CreatePortfolioRequest request) {
        validateName(request.name());
        validateStartingCredit(request.credit());
    }

    private void validateUpdatePortfolioRequest(UpdatePortfolioRequest request) {
        validateName(request.newName());
    }

    private void validateName(String name) {
        if(name == null || name.isBlank()) {
            throw new ValidationException(
                    "Portfolio name is required.",
                    "Please enter a name for the portfolio."
            );
        }

        if(name.length() < 2 || name.length() > 25) {
            throw new ValidationException(
                    "Portfolio name is invalid.",
                    "The name must contain between 2 and 25 characters.");
        }
    }

    private void validateStartingCredit(BigDecimal credit) {
        if(credit == null || credit.compareTo(BigDecimal.valueOf(500000)) != 0) {
            throw new ValidationException(
                    "Starting credit is invalid.",
                    "The starting credit must be exactly $500,000.");
        }
    }

}
