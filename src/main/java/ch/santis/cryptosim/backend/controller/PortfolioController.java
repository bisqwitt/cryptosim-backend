package ch.santis.cryptosim.backend.controller;

import ch.santis.cryptosim.backend.dto.portfolio.CreatePortfolioRequest;
import ch.santis.cryptosim.backend.dto.portfolio.PortfolioResponse;
import ch.santis.cryptosim.backend.service.PortfolioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/all")
    public List<PortfolioResponse> getAll() {
        return portfolioService.getAll();
    }

    @PostMapping
    public ResponseEntity<PortfolioResponse> create(@RequestBody CreatePortfolioRequest request) {
        PortfolioResponse savedPortfolio = portfolioService.createPortfolio(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPortfolio);
    }

}
