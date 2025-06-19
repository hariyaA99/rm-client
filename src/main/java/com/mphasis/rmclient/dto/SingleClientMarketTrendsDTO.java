// File: src/main/java/com/mphasis/rmclient/dto/SingleClientMarketTrendsDto.java
package com.mphasis.rmclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleClientMarketTrendsDTO {
    private String symbol;
    private String date;
    private BigDecimal closePrice;
}
