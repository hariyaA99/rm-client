package com.mphasis.rmclient.service.interfaces;

import com.mphasis.rmclient.dto.DashboardSummaryDto;
import com.mphasis.rmclient.dto.MarketDataDto;
import com.mphasis.rmclient.dto.PieChartDataDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface RmDashboardService {
    CompletableFuture<DashboardSummaryDto> getDashboardSummary(String rmCode);
    CompletableFuture<List<PieChartDataDto>> getPieChartData(String rmCode);
    CompletableFuture<MarketDataDto> getMarketTrends();
    CompletableFuture<Double> calculateTotalProfitPercentage(String rmCode);
    BigDecimal getTotalPortfolioValue(String rmCode);
    Integer getTotalClients(String rmCode);
    Integer getTotalUnitsHeld(String rmCode);
    CompletableFuture<DashboardSummaryDto> getNumericSummary(String rmCode);
}

