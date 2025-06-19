package com.mphasis.rmclient.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ClientPerformanceDto {
    private Integer clientId;
    private String clientName;
    private String clientEmail;
    private BigDecimal totalInvested;
    @JsonIgnore
    private BigDecimal totalCurrentValue;
    private Double profitPercentage;

    // Default constructor
    public ClientPerformanceDto() {}

    // Constructor with all fields including email
    public ClientPerformanceDto(Integer clientId, String clientName, String clientEmail,
                                BigDecimal totalInvested, BigDecimal totalCurrentValue,
                                Double profitPercentage) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.totalInvested = totalInvested;
        this.totalCurrentValue = totalCurrentValue;
        this.profitPercentage = profitPercentage;
    }

    // Backward compatibility constructor (without email)
    public ClientPerformanceDto(Integer clientId, String clientName, BigDecimal totalInvested,
                                BigDecimal totalCurrentValue, Double profitPercentage) {
        this(clientId, clientName, null, totalInvested, totalCurrentValue, profitPercentage);
    }

    // Getters and Setters
    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(BigDecimal totalInvested) {
        this.totalInvested = totalInvested;
    }

    public BigDecimal getTotalCurrentValue() {
        return totalCurrentValue;
    }

    public void setTotalCurrentValue(BigDecimal totalCurrentValue) {
        this.totalCurrentValue = totalCurrentValue;
    }

    public Double getProfitPercentage() {
        return profitPercentage;
    }

    public void setProfitPercentage(Double profitPercentage) {
        this.profitPercentage = profitPercentage;
    }

    @Override
    public String toString() {
        return "ClientPerformanceDto{" +
                "clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", totalInvested=" + totalInvested +
                ", totalCurrentValue=" + totalCurrentValue +
                ", profitPercentage=" + profitPercentage +
                '}';
    }
}