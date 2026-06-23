package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.client.CoinGeckoApiClient;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoPriceAtDateResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoPriceResponse;
import ch.santis.cryptosim.backend.dto.crypto.*;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketChartResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketResponse;
import ch.santis.cryptosim.backend.mapper.CryptoMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CryptoService {

    private final CoinGeckoApiClient coinGeckoApiClient;
    private final CryptoMapper cryptoMapper;

    public CryptoService(CoinGeckoApiClient coinGeckoApiClient, CryptoMapper cryptoMapper) {
        this.coinGeckoApiClient = coinGeckoApiClient;
        this.cryptoMapper = cryptoMapper;
    }

    public List<CryptoMarketDataResponse> getMarketData() {
        return coinGeckoApiClient.getMarkets()
                .stream()
                .map(cryptoMapper::toMarketDataResponse)
                .toList();
    }

    public CryptoMarketDataResponse getMarketData(String id) {
        return cryptoMapper.toMarketDataResponse(coinGeckoApiClient.getCoin(id));
    }

    public CryptoPriceResponse getPrice(String id, LocalDate date) {
        if (date != null) {
            return cryptoMapper.toPriceResponse(coinGeckoApiClient.getCoinPriceAtDate(id, date));
        }

        return cryptoMapper.toPriceResponse(id, coinGeckoApiClient.getCoinPrice(id).get(id));
    }

    public CryptoHistoricalDataResponse getHistoricalData(String id) {
        return cryptoMapper.toHistoricalDataResponse(
                coinGeckoApiClient.getMarketChart(id)
        );
    }

}
