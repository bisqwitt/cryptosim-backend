package ch.santis.cryptosim.backend.controller;

import ch.santis.cryptosim.backend.dto.crypto.CryptoHistoricalDataResponse;
import ch.santis.cryptosim.backend.dto.crypto.CryptoMarketDataResponse;
import ch.santis.cryptosim.backend.dto.crypto.CryptoPriceResponse;
import ch.santis.cryptosim.backend.service.CryptoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("/market")
    public List<CryptoMarketDataResponse> getMarketData() {
        return cryptoService.getMarketData();
    }

    @GetMapping("/{id}")
    public CryptoMarketDataResponse getMarketData(@PathVariable String id) {
        return cryptoService.getMarketData(id);
    }

    @GetMapping("/{id}/price")
    public CryptoPriceResponse getPrice(
            @PathVariable String id,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return cryptoService.getPrice(id, date);
    }

    @GetMapping("/{id}/history")
    public CryptoHistoricalDataResponse getHistoricalData(@PathVariable String id) {
        return cryptoService.getHistoricalData(id);
    }

}
