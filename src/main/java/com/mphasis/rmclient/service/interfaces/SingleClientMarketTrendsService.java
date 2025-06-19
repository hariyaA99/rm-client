package com.mphasis.rmclient.service.interfaces;

import com.mphasis.rmclient.dto.SingleClientMarketTrendsDTO;

import java.util.List;

public interface SingleClientMarketTrendsService {
    List<SingleClientMarketTrendsDTO> getLatestPricesForStocks(List<String> symbols);
}
