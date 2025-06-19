package com.mphasis.rmclient.service.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mphasis.rmclient.dto.SingleClientMarketTrendsDTO;
import com.mphasis.rmclient.service.interfaces.SingleClientMarketTrendsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SingleClientMarketTrendsServiceImpl implements SingleClientMarketTrendsService {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://www.alphavantage.co/query";

    @Override
    public List<SingleClientMarketTrendsDTO> getLatestPricesForStocks(List<String> symbols) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        List<SingleClientMarketTrendsDTO> result = new ArrayList<>();

        for (String symbol : symbols) {
            try {
                String url = BASE_URL + "?function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=" + apiKey;
                String response = restTemplate.getForObject(url, String.class);
                JsonNode root = mapper.readTree(response);
                JsonNode timeSeries = root.get("Time Series (Daily)");

                if (timeSeries != null) {
                    String latestDate = timeSeries.fieldNames().next();
                    BigDecimal closePrice = new BigDecimal(timeSeries.get(latestDate).get("4. close").asText());
                    result.add(new SingleClientMarketTrendsDTO(symbol, latestDate, closePrice));
                }
            } catch (Exception e) {
                System.err.println("Error fetching price for " + symbol + ": " + e.getMessage());
            }
        }

        return result;
    }
}
