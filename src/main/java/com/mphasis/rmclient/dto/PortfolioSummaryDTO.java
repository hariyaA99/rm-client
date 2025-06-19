// File: PortfolioSummaryDTO.java
package com.mphasis.rmclient.dto;

import java.util.Map;

public class PortfolioSummaryDTO {
    private int clientId;
    private Map<String, Double> allocation;
    private double totalInvestment;

    public PortfolioSummaryDTO(int clientId, Map<String, Double> allocation, double totalInvestment) {
        this.clientId = clientId;
        this.allocation = allocation;
        this.totalInvestment = totalInvestment;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Map<String, Double> getAllocation() {
        return allocation;
    }

    public void setAllocation(Map<String, Double> allocation) {
        this.allocation = allocation;
    }

    public double getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }
}
