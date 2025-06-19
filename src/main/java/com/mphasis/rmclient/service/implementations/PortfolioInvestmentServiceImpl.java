// File: PortfolioInvestmentServiceImpl.java
package com.mphasis.rmclient.service.implementations;

import com.mphasis.rmclient.dto.ClientPerformanceDto;
import com.mphasis.rmclient.repository.PortfolioInvestmentRepository;
import com.mphasis.rmclient.service.interfaces.PortfolioInvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioInvestmentServiceImpl implements PortfolioInvestmentService {

    @Autowired
    private PortfolioInvestmentRepository portfolioInvestmentRepository;

    @Override
    public List<ClientPerformanceDto> getTop5ClientsByRmCode(String rmCode) {
        List<Object[]> results = portfolioInvestmentRepository.findTop5ClientsByRmCode(rmCode);
        return convertToClientPerformanceDto(results);
    }

    @Override
    public List<ClientPerformanceDto> getBottom5ClientsByRmCode(String rmCode) {
        List<Object[]> results = portfolioInvestmentRepository.findBottom5ClientsByRmCode(rmCode);
        return convertToClientPerformanceDto(results);
    }

    private List<ClientPerformanceDto> convertToClientPerformanceDto(List<Object[]> results) {
        return results.stream().map(row -> {
            Integer clientId = (Integer) row[0];
            String clientName = (String) row[1];
            String clientEmail = (String) row[2];  // Now at index 2
            BigDecimal totalInvested = (BigDecimal) row[3];  // Shifted to index 3
            BigDecimal totalCurrentValue = (BigDecimal) row[4];  // Shifted to index 4
            Double profitPercentage = ((Number) row[5]).doubleValue();  // Shifted to index 5

            return new ClientPerformanceDto(
                    clientId,
                    clientName,
                    clientEmail,
                    totalInvested,
                    totalCurrentValue,
                    profitPercentage
            );
        }).collect(Collectors.toList());
    }
}
