package com.mphasis.rmclient.service.implementations;

import com.mphasis.rmclient.dto.DashboardSummaryDto;
import com.mphasis.rmclient.dto.MarketDataDto;
import com.mphasis.rmclient.dto.PieChartDataDto;
import com.mphasis.rmclient.entity.PortfolioInvestment;
import com.mphasis.rmclient.repository.PortfolioInvestmentRepository;
import com.mphasis.rmclient.repository.RMMappingRepository;
import com.mphasis.rmclient.service.interfaces.RmDashboardService;
import com.mphasis.rmclient.service.interfaces.AlphaVantageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class RmDashboardServiceImpl implements RmDashboardService {

    private static final Logger logger = LoggerFactory.getLogger(RmDashboardServiceImpl.class);

    @Autowired
    private PortfolioInvestmentRepository portfolioInvestmentRepository;

    @Autowired
    private RMMappingRepository RMMappingRepository;

    @Autowired
    private AlphaVantageService alphaVantageService;

    @Override
    public CompletableFuture<DashboardSummaryDto> getDashboardSummary(String rmCode) {
        logger.info("Generating dashboard summary for RM: {}", rmCode);

        CompletableFuture<List<PieChartDataDto>> pieChartFuture = getPieChartData(rmCode);
        CompletableFuture<Double> profitPercentageFuture = calculateTotalProfitPercentage(rmCode);

        return CompletableFuture.allOf(pieChartFuture, profitPercentageFuture)
                .thenApply(v -> {
                    DashboardSummaryDto summary = new DashboardSummaryDto();
                    summary.setRmCode(rmCode);
                    summary.setPieChartData(pieChartFuture.join());
                    summary.setTotalProfitPercentage(profitPercentageFuture.join());
                    summary.setTotalPortfolioValue(getTotalPortfolioValue(rmCode));
                    summary.setTotalClients(getTotalClients(rmCode));
                    summary.setTotalUnitsHeld(getTotalUnitsHeld(rmCode));
                    return summary;
                });
    }

    @Override
    public CompletableFuture<List<PieChartDataDto>> getPieChartData(String rmCode) {
        logger.info("Generating pie chart data for RM: {}", rmCode);

        return CompletableFuture.supplyAsync(() -> {
            List<Object[]> sectorData = portfolioInvestmentRepository.findSectorWiseInvestmentByRmCode(rmCode);
            BigDecimal totalInvestment = portfolioInvestmentRepository.findTotalInvestmentByRmCode(rmCode);

            if (totalInvestment == null || totalInvestment.equals(BigDecimal.ZERO)) {
                logger.warn("No investment data found for RM: {}", rmCode);
                return new ArrayList<>();
            }

            return sectorData.stream()
                    .map(row -> {
                        String sector = (String) row[0];
                        BigDecimal sectorInvestment = (BigDecimal) row[1];
                        Double percentage = sectorInvestment.divide(totalInvestment, 4, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal(100)).doubleValue();

                        return new PieChartDataDto(sector, sectorInvestment, percentage);
                    })
                    .collect(Collectors.toList());
        });
    }

    @Override
    public CompletableFuture<MarketDataDto> getMarketTrends() {
        return alphaVantageService.getMarketTrends();
    }

    @Override
    public CompletableFuture<Double> calculateTotalProfitPercentage(String rmCode) {
        logger.info("Calculating total profit percentage from database for RM: {}", rmCode);

        return CompletableFuture.supplyAsync(() -> {
            List<Object[]> portfolioWithEntities = portfolioInvestmentRepository.findPortfolioWithEntityNamesByRmCode(rmCode);

            if (portfolioWithEntities.isEmpty()) {
                logger.warn("No portfolio data found for RM: {}", rmCode);
                return 0.0;
            }

            BigDecimal totalCurrentValue = portfolioWithEntities.stream()
                    .map(row -> ((PortfolioInvestment) row[0]).getCurrentValue())
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalInvested = portfolioWithEntities.stream()
                    .map(row -> ((PortfolioInvestment) row[0]).getAmountInvested())
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalInvested.compareTo(BigDecimal.ZERO) == 0) {
                return 0.0;
            }

            BigDecimal profit = totalCurrentValue.subtract(totalInvested);
            BigDecimal profitPercentage = profit.divide(totalInvested, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));

            return profitPercentage.doubleValue();
        });
    }


    @Override
    public BigDecimal getTotalPortfolioValue(String rmCode) {
        BigDecimal totalInvestment = portfolioInvestmentRepository.findTotalInvestmentByRmCode(rmCode);
        return totalInvestment != null ? totalInvestment : BigDecimal.ZERO;
    }

    @Override
    public Integer getTotalClients(String rmCode) {
        Integer clientCount = RMMappingRepository.countClientsByRmCode(rmCode);
        return clientCount != null ? clientCount : 0;
    }

    @Override
    public Integer getTotalUnitsHeld(String rmCode) {
        Integer unitsHeld = portfolioInvestmentRepository.findTotalUnitsHeldByRmCode(rmCode);
        return unitsHeld != null ? unitsHeld : 0;
    }

    @Override
    public CompletableFuture<DashboardSummaryDto> getNumericSummary(String rmCode) {
        logger.info("Generating numeric summary for RM: {}", rmCode);

        CompletableFuture<Double> profitPercentageFuture = calculateTotalProfitPercentage(rmCode);
        CompletableFuture<List<PieChartDataDto>> pieChartFuture = getPieChartData(rmCode);

        return CompletableFuture.allOf(profitPercentageFuture, pieChartFuture)
                .thenApply(v -> {
                    DashboardSummaryDto summary = new DashboardSummaryDto();
                    summary.setRmCode(rmCode);
                    summary.setTotalPortfolioValue(getTotalPortfolioValue(rmCode));
                    summary.setTotalClients(getTotalClients(rmCode));
                    summary.setTotalUnitsHeld(getTotalUnitsHeld(rmCode));
                    summary.setTotalProfitPercentage(profitPercentageFuture.join());
                    summary.setPieChartData(pieChartFuture.join());
                    return summary;
                });
    }
}