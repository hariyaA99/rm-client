// File: PortfolioServiceImpl.java
package com.mphasis.rmclient.service.implementations;

import com.mphasis.rmclient.dto.PortfolioSummaryDTO;
import com.mphasis.rmclient.entity.PortfolioInvestment;
import com.mphasis.rmclient.entity.RMMapping;
import com.mphasis.rmclient.exception.ResourceNotFoundException;
import com.mphasis.rmclient.repository.PortfolioInvestmentRepository;
import com.mphasis.rmclient.repository.RMMappingRepository;
import com.mphasis.rmclient.service.interfaces.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioInvestmentRepository investmentRepository;

    @Autowired
    private RMMappingRepository RMMappingRepository;

    @Override
    public PortfolioSummaryDTO getPortfolioSummary(int clientId) {
        RMMapping client = RMMappingRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        List<PortfolioInvestment> investments = investmentRepository.findByClient_ClientId(clientId);

        if (investments.isEmpty()) {
            throw new ResourceNotFoundException("No investments found for client ID: " + clientId);
        }

        double total = investments.stream()
                .mapToDouble(inv -> inv.getAmountInvested().doubleValue())
                .sum();

        Map<String, Double> allocation = investments.stream()
                .collect(Collectors.groupingBy(
                        inv -> inv.getCategory().getCategoryName(),
                        Collectors.summingDouble(inv -> inv.getAmountInvested().doubleValue())
                ));

        return new PortfolioSummaryDTO(clientId, allocation, total);
    }
}
