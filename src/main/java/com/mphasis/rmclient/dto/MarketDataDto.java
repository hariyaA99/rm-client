package com.mphasis.rmclient.dto;

import java.util.List;

public class MarketDataDto {
    private List<StockQuoteDto> topGainers;
    private List<StockQuoteDto> topLosers;

    // Constructors
    public MarketDataDto() {}

    public MarketDataDto(List<StockQuoteDto> topGainers, List<StockQuoteDto> topLosers) {
        this.topGainers = topGainers;
        this.topLosers = topLosers;
    }

    // Getters and Setters
    public List<StockQuoteDto> getTopGainers() { return topGainers; }
    public void setTopGainers(List<StockQuoteDto> topGainers) { this.topGainers = topGainers; }

    public List<StockQuoteDto> getTopLosers() { return topLosers; }
    public void setTopLosers(List<StockQuoteDto> topLosers) { this.topLosers = topLosers; }
}
