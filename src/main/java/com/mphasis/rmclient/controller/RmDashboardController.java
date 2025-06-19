package com.mphasis.rmclient.controller;

import com.mphasis.rmclient.dto.ClientPerformanceDto;
import com.mphasis.rmclient.dto.DashboardSummaryDto;
import com.mphasis.rmclient.dto.MarketDataDto;
import com.mphasis.rmclient.dto.PieChartDataDto;
import com.mphasis.rmclient.entity.RelationshipManager;
import com.mphasis.rmclient.service.interfaces.EmailService;
import com.mphasis.rmclient.service.interfaces.RelationshipManagerService;
import com.mphasis.rmclient.service.interfaces.RmDashboardService;
import com.mphasis.rmclient.service.interfaces.TokenValidatorService;
import com.mphasis.rmclient.service.interfaces.PortfolioInvestmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/rm")
@CrossOrigin(origins = "*")
public class RmDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(RmDashboardController.class);

    @Autowired
    private RmDashboardService rmDashboardService;

    @Autowired
    private RelationshipManagerService relationshipManagerService;

    @Autowired
    private TokenValidatorService tokenValidatorService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PortfolioInvestmentService portfolioInvestmentService;

    // Helper method to get RM code from session
    private String getRmCodeFromHeaders(HttpHeaders headers) {
        tokenValidatorService.validateToken(headers);
        return tokenValidatorService.getRmCodeFromSession(headers);
    }

    @GetMapping("/dashboard")
    public CompletableFuture<ResponseEntity<DashboardSummaryDto>> getDashboard(
            @RequestHeader HttpHeaders headers) {

        String rmCode = getRmCodeFromHeaders(headers);

        return rmDashboardService.getDashboardSummary(rmCode)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    logger.error("Error in dashboard for RM: {}", rmCode, ex);
                    return new ResponseEntity<>((DashboardSummaryDto) null, HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @GetMapping("/market-trends")
    public CompletableFuture<ResponseEntity<MarketDataDto>> getMarketTrends(@RequestHeader HttpHeaders headers) {
        tokenValidatorService.validateToken(headers);
        return rmDashboardService.getMarketTrends()
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    logger.error("Error in market trends", ex);
                    return new ResponseEntity<>((MarketDataDto) null, HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @GetMapping("/pie-chart")
    public CompletableFuture<ResponseEntity<List<PieChartDataDto>>> getPieChartData(
            @RequestHeader HttpHeaders headers) {

        String rmCode = getRmCodeFromHeaders(headers);

        return rmDashboardService.getPieChartData(rmCode)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    logger.error("Error in pie chart for RM: {}", rmCode, ex);
                    return new ResponseEntity<>((List<PieChartDataDto>) null, HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @GetMapping("/summary")
    public CompletableFuture<ResponseEntity<DashboardSummaryDto>> getNumericSummary(
            @RequestHeader HttpHeaders headers) {

        String rmCode = getRmCodeFromHeaders(headers);

        return rmDashboardService.getNumericSummary(rmCode)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    logger.error("Error in numeric summary for RM: {}", rmCode, ex);
                    return new ResponseEntity<>((DashboardSummaryDto) null, HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @GetMapping("/profit-percentage")
    public CompletableFuture<ResponseEntity<Double>> getProfitPercentage(
            @RequestHeader HttpHeaders headers) {

        String rmCode = getRmCodeFromHeaders(headers);

        return rmDashboardService.calculateTotalProfitPercentage(rmCode)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    logger.error("Error in profit percentage for RM: {}", rmCode, ex);
                    return new ResponseEntity<>((Double) null, HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @GetMapping("/portfolio-value")
    public ResponseEntity<BigDecimal> getPortfolioValue(@RequestHeader HttpHeaders headers) {
        String rmCode = getRmCodeFromHeaders(headers);

        try {
            return ResponseEntity.ok(rmDashboardService.getTotalPortfolioValue(rmCode));
        } catch (Exception ex) {
            logger.error("Error in portfolio value for RM: {}", rmCode, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/total-clients")
    public ResponseEntity<Integer> getTotalClients(@RequestHeader HttpHeaders headers) {
        String rmCode = getRmCodeFromHeaders(headers);

        try {
            return ResponseEntity.ok(rmDashboardService.getTotalClients(rmCode));
        } catch (Exception ex) {
            logger.error("Error in total clients for RM: {}", rmCode, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/total-units")
    public ResponseEntity<Integer> getTotalUnits(@RequestHeader HttpHeaders headers) {
        String rmCode = getRmCodeFromHeaders(headers);

        try {
            return ResponseEntity.ok(rmDashboardService.getTotalUnitsHeld(rmCode));
        } catch (Exception ex) {
            logger.error("Error in total units for RM: {}", rmCode, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // New endpoint to get RM profile (name, code, etc.)
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getRmProfile(@RequestHeader HttpHeaders headers) {
        String username = headers.getFirst("Username");
        tokenValidatorService.validateToken(headers);

        return relationshipManagerService.getRmByEmail(username)
                .map(rm -> {
                    Map<String, Object> profile = new HashMap<>();
                    profile.put("rmCode", rm.getRmCode());
                    profile.put("name", rm.getName());
                    profile.put("email", rm.getEmail());
                    profile.put("phone", rm.getPhone());
                    profile.put("location", rm.getLocation());
                    return ResponseEntity.ok(profile);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/total-leads")
    public ResponseEntity<Long> getTotalLeads(@RequestHeader HttpHeaders headers) {
        tokenValidatorService.validateToken(headers);
        long totalLeads = emailService.getTotalLeads();
        return ResponseEntity.ok(totalLeads);
    }

    @GetMapping("/top5-clients")
    public ResponseEntity<List<ClientPerformanceDto>> getTop5Clients(@RequestHeader HttpHeaders headers) {
        String rmCode = getRmCodeFromHeaders(headers);

        try {
            List<ClientPerformanceDto> results = portfolioInvestmentService.getTop5ClientsByRmCode(rmCode);
            System.out.println(results);
            return ResponseEntity.ok(results);
        } catch (Exception ex) {
            logger.error("Error in top5 clients for RM: {}", rmCode, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bottom5-clients")
    public ResponseEntity<List<ClientPerformanceDto>> getBottom5Clients(@RequestHeader HttpHeaders headers) {
        String rmCode = getRmCodeFromHeaders(headers);

        try {
            List<ClientPerformanceDto> results = portfolioInvestmentService.getBottom5ClientsByRmCode(rmCode);
            return ResponseEntity.ok(results);
        } catch (Exception ex) {
            logger.error("Error in bottom5 clients for RM: {}", rmCode, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("RM Dashboard API is healthy");
    }
}