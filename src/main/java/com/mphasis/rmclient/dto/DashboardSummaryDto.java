package com.mphasis.rmclient.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardSummaryDto {
    private String rmCode;
    private BigDecimal totalPortfolioValue;
    private Integer totalClients;
    private Double totalProfitPercentage;
    private Integer totalUnitsHeld;
    private List<PieChartDataDto> pieChartData;

    // Constructors
    public DashboardSummaryDto() {}

    // Getters and Setters
    public String getRmCode() { return rmCode; }
    public void setRmCode(String rmCode) { this.rmCode = rmCode; }

    public BigDecimal getTotalPortfolioValue() { return totalPortfolioValue; }
    public void setTotalPortfolioValue(BigDecimal totalPortfolioValue) { this.totalPortfolioValue = totalPortfolioValue; }

    public Integer getTotalClients() { return totalClients; }
    public void setTotalClients(Integer totalClients) { this.totalClients = totalClients; }

    public Double getTotalProfitPercentage() { return totalProfitPercentage; }
    public void setTotalProfitPercentage(Double totalProfitPercentage) { this.totalProfitPercentage = totalProfitPercentage; }

    public Integer getTotalUnitsHeld() { return totalUnitsHeld; }
    public void setTotalUnitsHeld(Integer totalUnitsHeld) { this.totalUnitsHeld = totalUnitsHeld; }

    public List<PieChartDataDto> getPieChartData() { return pieChartData; }
    public void setPieChartData(List<PieChartDataDto> pieChartData) { this.pieChartData = pieChartData; }
}