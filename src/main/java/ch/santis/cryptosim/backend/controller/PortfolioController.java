package ch.santis.cryptosim.backend.controller;

import ch.santis.cryptosim.backend.dto.portfolio.*;
import ch.santis.cryptosim.backend.service.PortfolioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @PatchMapping("/{id}")
    public ResponseEntity<PortfolioResponse> update(
            @PathVariable Long id,
            @RequestBody UpdatePortfolioRequest request) {
        PortfolioResponse updatedPortfolio = portfolioService.updatePortfolio(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedPortfolio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/{id}")
    public PortfolioDetailsResponse getPortfolioDetails(@PathVariable Long id) {
        return portfolioService.getPortfolioDetails(id);
    }

    @GetMapping("/{id}/{cryptoId}/holding")
    public PortfolioPositionHoldingResponse getHoldingOfPosition(
            @PathVariable Long id,
            @PathVariable String cryptoId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate date) {
        return date == null ? null /*TODO*/ : portfolioService.getHoldingOfPosition(id, cryptoId, date);
    }

}
