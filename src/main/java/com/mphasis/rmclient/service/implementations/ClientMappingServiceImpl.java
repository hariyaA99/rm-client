// File: com.mphasis.rmclient.service.implementations.ClientMappingServiceImpl.java
package com.mphasis.rmclient.service.implementations;

import com.mphasis.rmclient.dto.ClientMappingSummaryDTO;
import com.mphasis.rmclient.repository.PortfolioInvestmentRepository;
import com.mphasis.rmclient.service.interfaces.ClientMappingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientMappingServiceImpl implements ClientMappingService {

    private final PortfolioInvestmentRepository PortfolioInvestmentRepository;

    // ✅ Add this constructor
    public ClientMappingServiceImpl(PortfolioInvestmentRepository PortfolioInvestmentRepository) {
        this.PortfolioInvestmentRepository = PortfolioInvestmentRepository;
    }

    @Override
    public List<ClientMappingSummaryDTO> getClientsByRmCode(String rmCode) {
        return PortfolioInvestmentRepository.findInvestmentsByRmCode(rmCode);
    }
}
