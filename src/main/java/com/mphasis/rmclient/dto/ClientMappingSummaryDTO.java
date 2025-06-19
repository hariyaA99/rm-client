package com.mphasis.rmclient.dto;

import java.math.BigDecimal;

public class ClientMappingSummaryDTO {
    private Integer clientId;
    private String name;
    private String email;
    private BigDecimal totalInvestment;

    public ClientMappingSummaryDTO(Integer clientId, String name, String email, BigDecimal totalInvestment) {
        this.clientId = clientId;
        this.name = name;
        this.email = email;
        this.totalInvestment = totalInvestment;
    }

    public Integer getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getTotalInvestment() {
        return totalInvestment;
    }
}