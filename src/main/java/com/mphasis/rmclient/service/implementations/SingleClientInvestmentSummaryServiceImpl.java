// Filename: SingleClientInvestmentSummaryServiceImpl.java
package com.mphasis.rmclient.service.implementations;

import com.mphasis.rmclient.dto.SingleClientInvestmentSummaryDTO;
import com.mphasis.rmclient.repository.PortfolioInvestmentRepository;
import com.mphasis.rmclient.service.interfaces.SingleClientInvestmentSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class SingleClientInvestmentSummaryServiceImpl implements SingleClientInvestmentSummaryService {

    @Autowired
    private PortfolioInvestmentRepository investmentRepository;

    @Override
    public SingleClientInvestmentSummaryDTO getInvestmentSummary(int clientId) {
        Object rawResult = investmentRepository.getClientInvestmentSummary((long) clientId);

        if (rawResult == null) {
            return null; // Or throw a custom exception
        }

        Object[] data = (Object[]) rawResult;

        String name = (String) data[0];
        String email = (String) data[1];
        BigDecimal amountInvested = (BigDecimal) data[2];
        BigDecimal currentValue = (BigDecimal) data[3];
        Long unitsHeldLong = (data[4] != null) ? ((Number) data[4]).longValue() : 0L;
        int unitsHeld = unitsHeldLong.intValue();

        BigDecimal netGain = BigDecimal.ZERO;
        if (amountInvested != null && amountInvested.compareTo(BigDecimal.ZERO) > 0) {
            netGain = currentValue.subtract(amountInvested)
                    .divide(amountInvested, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        return new SingleClientInvestmentSummaryDTO(
                clientId,
                name,
                email,
                amountInvested,
                currentValue,
                unitsHeld,
                netGain.setScale(2, RoundingMode.HALF_UP)
        );
    }
}
