package com.mphasis.rmclient.service.implementations;

import com.mphasis.rmclient.dto.AlphaVantageResponse;
import com.mphasis.rmclient.dto.MarketDataDto;
import com.mphasis.rmclient.dto.StockQuoteDto;
import com.mphasis.rmclient.service.interfaces.AlphaVantageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AlphaVantageServiceImpl implements AlphaVantageService {

    private static final Logger logger = LoggerFactory.getLogger(AlphaVantageServiceImpl.class);

    @Value("${alphavantage.api.key}")
    private String apiKey;

    private final WebClient webClient;

    private final List<String> popularStocks = Arrays.asList(
            "RELIANCE.BSE",     // Reliance Industries
            "TCS.BSE",          // Tata Consultancy Services
            "INFY.BSE",         // Infosys
            "HDFCBANK.BSE",     // HDFC Bank
            "ICICIBANK.BSE",    // ICICI Bank
            "KOTAKBANK.BSE",    // Kotak Mahindra Bank
            "LT.BSE",           // Larsen & Toubro
            "SBIN.BSE",         // State Bank of India
            "BHARTIARTL.BSE",   // Bharti Airtel
            "ASIANPAINT.BSE",   // Asian Paints
            "BAJAJAUTO.BSE",    // Bajaj Auto
            "BAJFINANCE.BSE",   // Bajaj Finance
            "HCLTECH.BSE",      // HCL Technologies
            "MARUTI.BSE",       // Maruti Suzuki
            "ULTRACEMCO.BSE",   // UltraTech Cement
            "SUNPHARMA.BSE",    // Sun Pharma
            "WIPRO.BSE",        // Wipro
            "TITAN.BSE",        // Titan Company
            "NESTLEIND.BSE",    // Nestlé India
            "ONGC.BSE",         // Oil and Natural Gas Corporation
            "POWERGRID.BSE",    // Power Grid Corporation
            "EICHERMOT.BSE",    // Eicher Motors
            "ITC.BSE",          // ITC Ltd.
            "TECHM.BSE",        // Tech Mahindra
            "COALINDIA.BSE",    // Coal India
            "HINDUNILVR.BSE",   // Hindustan Unilever
            "GRASIM.BSE",       // Grasim Industries
            "BHARATFORG.BSE",   // Bharat Forge (if currently in index)
            "NTPC.BSE",         // NTPC Ltd
            "ADANIPORTS.BSE",   // Adani Ports & SEZ
            "JSWSTEEL.BSE",     // JSW Steel
            "BAJAJFINSV.BSE",   // Bajaj Finserv
            "ASIANPAINT.BSE",   // already included
            "DRREDDY.BSE",      // Dr. Reddy’s Laboratories
            "UPL.BSE",          // UPL
            "BPCL.BSE",         // Bharat Petroleum Corp
            "HDFCLIFE.BSE",     // HDFC Life
            "INDUSINDBK.BSE",   // IndusInd Bank
            "ADANIENT.BSE",     // Adani Enterprises
            "APOLLOHOSP.BSE",   // Apollo Hospitals
            "BRITANNIA.BSE",    // Britannia Industries
            "CIPLA.BSE",        // Cipla
            "DIVISLAB.BSE",     // Divi's Laboratories
            "ULTRACEMCO.BSE",   // Duplicate already
            "TATAMOTORS.BSE",   // Tata Motors
            "TATASTEEL.BSE",    // Tata Steel
            "NTPC.BSE",
            "Mphasis.BSE" // Duplicate
    );


    public AlphaVantageServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public CompletableFuture<BigDecimal> getStockPrice(String symbol) {
        logger.info("Fetching stock price for symbol: {}", symbol);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function", "GLOBAL_QUOTE")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(AlphaVantageResponse.class)
                .map(response -> {
                    if (response.getGlobalQuote() != null && response.getGlobalQuote().getPrice() != null) {
                        return safeBigDecimalParse(response.getGlobalQuote().getPrice());
                    }
                    logger.warn("No price data found for symbol: {}", symbol);
                    return BigDecimal.ZERO;
                })
                .onErrorReturn(BigDecimal.ZERO)
                .toFuture();
    }

    @Override
    public CompletableFuture<MarketDataDto> getMarketTrends() {
        logger.info("Fetching market trends data");

        List<CompletableFuture<StockQuoteDto>> futures = popularStocks.stream()
                .map(this::getStockQuoteAsync)
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    List<StockQuoteDto> allQuotes = futures.stream()
                            .map(CompletableFuture::join)
                            .filter(quote -> quote != null && quote.getChangePercent() != null)
                            .collect(Collectors.toList());

                    List<StockQuoteDto> gainers = allQuotes.stream()
                            .filter(q -> parseChangePercent(q.getChangePercent()) > 0)
                            .sorted(Comparator.comparingDouble((StockQuoteDto quote) -> parseChangePercent(quote.getChangePercent())).reversed())
                            .collect(Collectors.toList());

                    List<StockQuoteDto> losers = allQuotes.stream()
                            .filter(q -> parseChangePercent(q.getChangePercent()) < 0)
                            .sorted(Comparator.comparingDouble((StockQuoteDto quote) -> parseChangePercent(quote.getChangePercent())))
                            .collect(Collectors.toList());

                    // Always return exactly 5 items
                    List<StockQuoteDto> topGainers = padList(gainers, 5);
                    List<StockQuoteDto> topLosers = padList(losers, 5);

                    return new MarketDataDto(topGainers, topLosers);
                })
                .exceptionally(throwable -> {
                    logger.error("Error fetching market trends", throwable);
                    return new MarketDataDto(new ArrayList<>(), new ArrayList<>());
                });
    }

    // Helper function to pad list
    private List<StockQuoteDto> padList(List<StockQuoteDto> list, int size) {
        List<StockQuoteDto> result = new ArrayList<>(list);
        while (result.size() < size) {
            result.add(createDefaultStockQuote("N/A"));
        }
        return result;
    }


    @Override
    public CompletableFuture<BigDecimal> getCurrentValue(String symbol, Integer unitsHeld) {
        return getStockPrice(symbol)
                .thenApply(price -> price.multiply(new BigDecimal(unitsHeld)));
    }

    private CompletableFuture<StockQuoteDto> getStockQuoteAsync(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("function", "GLOBAL_QUOTE")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(AlphaVantageResponse.class)
                .map(response -> {
                    if (response.getGlobalQuote() != null) {
                        var quote = response.getGlobalQuote();
                        return new StockQuoteDto(
                                quote.getSymbol(),
                                safeBigDecimalParse(quote.getPrice()),
                                safeBigDecimalParse(quote.getChange()),
                                quote.getChangePercent(),
                                safeBigDecimalParse(quote.getVolume()),
                                quote.getLatestTradingDay()
                        );
                    }
                    return createDefaultStockQuote(symbol);
                })
                .onErrorResume(throwable -> {
                    logger.error("Failed to fetch stock quote for symbol: {}", symbol, throwable);
                    return Mono.just(createDefaultStockQuote(symbol));
                })
                .toFuture();
    }

    private StockQuoteDto createDefaultStockQuote(String symbol) {
        return new StockQuoteDto(
                symbol,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "0.00%",
                BigDecimal.ZERO,
                LocalDate.now().toString()
        );
    }

    private Double parseChangePercent(String changePercent) {
        if (changePercent == null || changePercent.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(changePercent.replace("%", ""));
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse change percent: {}", changePercent);
            return 0.0;
        }
    }

    private BigDecimal safeBigDecimalParse(String value) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            logger.warn("Failed to parse BigDecimal from value: {}", value);
            return BigDecimal.ZERO;
        }
    }
}
