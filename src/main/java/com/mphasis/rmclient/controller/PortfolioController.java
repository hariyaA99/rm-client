// File: PortfolioController.java
package com.mphasis.rmclient.controller;

import com.mphasis.rmclient.dto.PortfolioSummaryDTO;
import com.mphasis.rmclient.service.interfaces.PortfolioService;
import com.mphasis.rmclient.service.interfaces.TokenValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private TokenValidatorService tokenValidatorService;

    // Validate token helper
    private void validateRequest(HttpHeaders headers) {
        tokenValidatorService.validateToken(headers);
    }

    @GetMapping("/summary/{clientId}")
    public PortfolioSummaryDTO getPortfolioSummary(
            @RequestHeader HttpHeaders headers,
            @PathVariable String clientId) {

        logger.info("Received request for portfolio summary with clientId: [{}]", clientId);
        validateRequest(headers);

        try {
            int parsedId = Integer.parseInt(clientId.trim());
            logger.debug("Parsed clientId as integer: {}", parsedId);

            PortfolioSummaryDTO summary = portfolioService.getPortfolioSummary(parsedId);
            logger.info("Fetched portfolio summary for clientId {}: {}", parsedId, summary);
            return summary;
        } catch (NumberFormatException e) {
            logger.error("Invalid clientId format received: [{}]", clientId, e);
            throw new IllegalArgumentException("Client ID must be a valid number");
        } catch (Exception e) {
            logger.error("Unexpected error while fetching portfolio summary for clientId [{}]", clientId, e);
            throw e;
        }
    }
}
