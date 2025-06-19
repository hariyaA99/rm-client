package com.mphasis.rmclient.dto;

import java.math.BigDecimal;

public class PieChartDataDto {
    private String sector;
    private BigDecimal totalInvestment;
    private Double percentage;

    // Constructors
    public PieChartDataDto() {}

    public PieChartDataDto(String sector, BigDecimal totalInvestment, Double percentage) {
        this.sector = sector;
        this.totalInvestment = totalInvestment;
        this.percentage = percentage;
    }

    // Getters and Setters
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public BigDecimal getTotalInvestment() { return totalInvestment; }
    public void setTotalInvestment(BigDecimal totalInvestment) { this.totalInvestment = totalInvestment; }

    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
}
