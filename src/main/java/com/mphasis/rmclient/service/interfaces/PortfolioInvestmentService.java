package com.mphasis.rmclient.service.interfaces;

import com.mphasis.rmclient.dto.ClientPerformanceDto;
import java.util.List;

public interface PortfolioInvestmentService {

    List<ClientPerformanceDto> getTop5ClientsByRmCode(String rmCode);

    List<ClientPerformanceDto> getBottom5ClientsByRmCode(String rmCode);
}