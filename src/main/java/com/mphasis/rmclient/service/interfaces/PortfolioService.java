// File: PortfolioService.java
package com.mphasis.rmclient.service.interfaces;

import com.mphasis.rmclient.dto.PortfolioSummaryDTO;

public interface PortfolioService {
    PortfolioSummaryDTO getPortfolioSummary(int clientId);
}
