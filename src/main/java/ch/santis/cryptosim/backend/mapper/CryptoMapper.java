package ch.santis.cryptosim.backend.mapper;

import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketChartResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoMarketResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoPriceAtDateResponse;
import ch.santis.cryptosim.backend.dto.coingecko.CoinGeckoPriceResponse;
import ch.santis.cryptosim.backend.dto.crypto.CryptoHistoricalDataPoint;
import ch.santis.cryptosim.backend.dto.crypto.CryptoHistoricalDataResponse;
import ch.santis.cryptosim.backend.dto.crypto.CryptoMarketDataResponse;
import ch.santis.cryptosim.backend.dto.crypto.CryptoPriceResponse;
import org.springframework.stereotype.Component;

@Component
public class CryptoMapper {

    public CryptoMarketDataResponse toMarketDataResponse(CoinGeckoMarketResponse source) {
        return new CryptoMarketDataResponse(
                source.id(),
                source.name(),
                source.symbol(),
                source.currentPrice(),
                source.priceChangePercentage24h(),
                source.priceChangePercentage7d(),
                source.marketCap(),
                source.totalVolume()
        );
    }

    public CryptoPriceResponse toPriceResponse(
            CoinGeckoPriceAtDateResponse source) {

        return new CryptoPriceResponse(
                source.id(),
                source.marketData().currentPrice().usd()
        );
    }

    public CryptoPriceResponse toPriceResponse(
            String cryptoId,
            CoinGeckoPriceResponse source) {

        return new CryptoPriceResponse(
                cryptoId,
                source.usd()
        );
    }

    public CryptoHistoricalDataResponse toHistoricalDataResponse(
            CoinGeckoMarketChartResponse source) {

        return new CryptoHistoricalDataResponse(
                source.prices().stream().map(this::toHistoricalDataPoint).toList(),
                source.marketCaps().stream().map(this::toHistoricalDataPoint).toList(),
                source.totalVolumes().stream().map(this::toHistoricalDataPoint).toList()
        );
    }

    private CryptoHistoricalDataPoint toHistoricalDataPoint(
            CoinGeckoMarketChartResponse.MarketChartPoint point) {

        return new CryptoHistoricalDataPoint(
                point.timestamp(),
                point.value()
        );
    }
}
