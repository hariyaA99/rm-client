package com.mphasis.rmclient.dto;

import java.math.BigDecimal;

public class StockQuoteDto {
    private String symbol;
    private BigDecimal price;
    private BigDecimal change;
    private String changePercent;
    private BigDecimal volume;
    private String latestTradingDay;

    // Constructors
    public StockQuoteDto() {}

    public StockQuoteDto(String symbol, BigDecimal price, BigDecimal change, String changePercent, BigDecimal volume, String latestTradingDay) {
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
        this.volume = volume;
        this.latestTradingDay = latestTradingDay;
    }

    // Getters and Setters
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getChange() { return change; }
    public void setChange(BigDecimal change) { this.change = change; }

    public String getChangePercent() { return changePercent; }
    public void setChangePercent(String changePercent) { this.changePercent = changePercent; }

    public BigDecimal getVolume() { return volume; }
    public void setVolume(BigDecimal volume) { this.volume = volume; }

    public String getLatestTradingDay() { return latestTradingDay; }
    public void setLatestTradingDay(String latestTradingDay) { this.latestTradingDay = latestTradingDay; }
}

