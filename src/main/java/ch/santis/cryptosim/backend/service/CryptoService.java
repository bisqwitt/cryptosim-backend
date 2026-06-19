package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.client.CoinGeckoApiClient;
import ch.santis.cryptosim.backend.dto.CryptoMarketDataDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoService {

    private final CoinGeckoApiClient coinGeckoApiClient;

    public CryptoService(CoinGeckoApiClient coinGeckoApiClient) {
        this.coinGeckoApiClient = coinGeckoApiClient;
    }

    public List<CryptoMarketDataDto> getMarketData() {
        return coinGeckoApiClient.getMarketData();
    }

}
