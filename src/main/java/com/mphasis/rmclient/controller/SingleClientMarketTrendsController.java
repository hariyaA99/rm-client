// File: controller/SingleClientMarketTrendsController.java
package com.mphasis.rmclient.controller;

import com.mphasis.rmclient.dto.SingleClientMarketTrendsDTO;
import com.mphasis.rmclient.service.interfaces.SingleClientMarketTrendsService;
import com.mphasis.rmclient.service.interfaces.TokenValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/single-client/market-trends")
@CrossOrigin(origins = "*")
public class SingleClientMarketTrendsController {

    @Autowired
    private SingleClientMarketTrendsService marketTrendsService;

    @Autowired
    private TokenValidatorService tokenValidatorService;

    private void validateRequest(HttpHeaders headers) {
        tokenValidatorService.validateToken(headers);
    }

    @GetMapping
    public List<SingleClientMarketTrendsDTO> getMultipleStockTrends(@RequestHeader HttpHeaders headers) {
        validateRequest(headers);

        List<String> symbols = Arrays.asList(
                "MPHASIS.BSE",
                "RELIANCE.BSE",
                "INFY.BSE",
                "HDFC.BSE",
                "ICICIBANK.BSE",
                "TCS.BSE",
                "KOTAKBANK.NS"
        );
        return marketTrendsService.getLatestPricesForStocks(symbols);
    }
}
