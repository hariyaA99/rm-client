// Filename: SingleClientInvestmentSummaryService.java
package com.mphasis.rmclient.service.interfaces;

import com.mphasis.rmclient.dto.SingleClientInvestmentSummaryDTO;

public interface SingleClientInvestmentSummaryService {
    SingleClientInvestmentSummaryDTO getInvestmentSummary(int clientId);
}
