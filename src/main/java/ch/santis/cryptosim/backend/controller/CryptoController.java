package ch.santis.cryptosim.backend.controller;

import ch.santis.cryptosim.backend.dto.CryptoMarketDataDto;
import ch.santis.cryptosim.backend.service.CryptoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("/market")
    public List<CryptoMarketDataDto> getMarketData() {
        return cryptoService.getMarketData();
    }

}
