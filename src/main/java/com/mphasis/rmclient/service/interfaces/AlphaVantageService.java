package com.mphasis.rmclient.service.interfaces;


import com.mphasis.rmclient.dto.MarketDataDto;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public interface AlphaVantageService {
    CompletableFuture<BigDecimal> getStockPrice(String symbol);

    CompletableFuture<MarketDataDto> getMarketTrends();

    CompletableFuture<BigDecimal> getCurrentValue(String symbol, Integer unitsHeld);
}
