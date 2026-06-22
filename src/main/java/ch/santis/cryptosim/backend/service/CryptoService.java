package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.client.CoinGeckoApiClient;
import ch.santis.cryptosim.backend.dto.crypto.CryptoHistoricalData;
import ch.santis.cryptosim.backend.dto.crypto.CryptoHistoricalDataPoint;
import ch.santis.cryptosim.backend.dto.crypto.CryptoMarketData;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketChartResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoService {

    private final CoinGeckoApiClient coinGeckoApiClient;

    public CryptoService(CoinGeckoApiClient coinGeckoApiClient) {
        this.coinGeckoApiClient = coinGeckoApiClient;
    }

    public List<CryptoMarketData> getMarketData() {
        List<CoinGeckoMarketResponse> response = coinGeckoApiClient.getMarkets();

        return response.stream()
                .map(crypto -> new CryptoMarketData(
                        crypto.id(),
                        crypto.name(),
                        crypto.symbol(),
                        crypto.currentPrice(),
                        crypto.priceChangePercentage24h(),
                        crypto.priceChangePercentage7d(),
                        crypto.marketCap(),
                        crypto.totalVolume()
                ))
                .toList();
    }

    public CryptoMarketData getMarketData(String id) {
        CoinGeckoMarketResponse response = coinGeckoApiClient.getCoin(id);

        return new CryptoMarketData(
                response.id(),
                response.name(),
                response.symbol(),
                response.currentPrice(),
                response.priceChangePercentage24h(),
                response.priceChangePercentage7d(),
                response.marketCap(),
                response.totalVolume()
        );
    }

    public CryptoHistoricalData getHistoricalData(String id) {
        CoinGeckoMarketChartResponse response = coinGeckoApiClient.getMarketChart(id);

        return new CryptoHistoricalData(
                response.prices().stream()
                        .map(point -> new CryptoHistoricalDataPoint(point.timestamp(), point.value()))
                        .toList(),

                response.marketCaps().stream()
                        .map(point -> new CryptoHistoricalDataPoint(point.timestamp(), point.value()))
                        .toList(),

                response.totalVolumes().stream()
                        .map(point -> new CryptoHistoricalDataPoint(point.timestamp(), point.value()))
                        .toList()
        );
    }

}
