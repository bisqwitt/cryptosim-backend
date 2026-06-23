package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.client.CoinGeckoApiClient;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoPriceAtDateResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoPriceResponse;
import ch.santis.cryptosim.backend.dto.crypto.*;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketChartResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CryptoService {

    private final CoinGeckoApiClient coinGeckoApiClient;

    public CryptoService(CoinGeckoApiClient coinGeckoApiClient) {
        this.coinGeckoApiClient = coinGeckoApiClient;
    }

    public List<CryptoMarketDataResponse> getMarketData() {
        List<CoinGeckoMarketResponse> response = coinGeckoApiClient.getMarkets();

        return response.stream()
                .map(crypto -> new CryptoMarketDataResponse(
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

    public CryptoMarketDataResponse getMarketData(String id) {
        CoinGeckoMarketResponse response = coinGeckoApiClient.getCoin(id);

        return new CryptoMarketDataResponse(
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

    public CryptoPriceResponse getPrice(String id, LocalDate date) {
        if(date != null) return getPriceAtDate(id, date);
        Map<String, CoinGeckoPriceResponse> response = coinGeckoApiClient.getCoinPrice(id);

        return new CryptoPriceResponse(
                id,
                response.get(id).usd()
        );
    }

    private CryptoPriceResponse getPriceAtDate(String id, LocalDate date) {
        CoinGeckoPriceAtDateResponse response = coinGeckoApiClient.getCoinPriceAtDate(id, date);

        return new CryptoPriceResponse(
                response.id(),
                response.marketData().currentPrice().usd()
        );
    }

    public CryptoHistoricalDataResponse getHistoricalData(String id) {
        CoinGeckoMarketChartResponse response = coinGeckoApiClient.getMarketChart(id);

        return new CryptoHistoricalDataResponse(
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
