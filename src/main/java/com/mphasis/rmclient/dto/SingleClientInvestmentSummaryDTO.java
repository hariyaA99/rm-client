// Filename: SingleClientInvestmentSummaryDTO.java
package com.mphasis.rmclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleClientInvestmentSummaryDTO {

    private int clientId;
    private String name;
    private String email;
    private BigDecimal amountInvested;
    private BigDecimal currentValue;
    private int unitsHeld;
    private BigDecimal netGainPercentage;
}
