package ch.santis.cryptosim.backend.service;

import ch.santis.cryptosim.backend.client.CoinGeckoApiClient;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoPriceAtDateResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoPriceResponse;
import ch.santis.cryptosim.backend.dto.crypto.*;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketChartResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketResponse;
import ch.santis.cryptosim.backend.error.CoinGeckoApiException;
import ch.santis.cryptosim.backend.error.ValidationException;
import ch.santis.cryptosim.backend.mapper.CryptoMapper;
import org.springframework.http.HttpStatus;
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
            validateDate(date);
            return cryptoMapper.toPriceResponse(coinGeckoApiClient.getCoinPriceAtDate(id, date));
        }

        Map<String, CoinGeckoPriceResponse> priceMap = coinGeckoApiClient.getCoinPrice(id);
        validatePriceMap(priceMap, id);
        return cryptoMapper.toPriceResponse(id, priceMap.get(id));
    }

    public CryptoHistoricalDataResponse getHistoricalData(String id) {
        return cryptoMapper.toHistoricalDataResponse(
                coinGeckoApiClient.getMarketChart(id)
        );
    }

    private void validateDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Date is invalid.",
                    "The date cannot be in the future."
            );
        }

        if (date.isBefore(LocalDate.now().minusDays(365))) {
            throw new ValidationException(
                    "Date is invalid.",
                    "The Date must be within the last 365 days."
            );
        }
    }

    private void validatePriceMap(Map<String, CoinGeckoPriceResponse> priceMap, String id) {
        if(!priceMap.containsKey(id)) {
            throw CoinGeckoApiException.from(HttpStatus.NOT_FOUND);
        }
    }

}
