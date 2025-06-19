package com.mphasis.rmclient.service.interfaces;

import com.mphasis.rmclient.dto.ClientMappingSummaryDTO;

import java.util.List;

public interface ClientMappingService {
    List<ClientMappingSummaryDTO> getClientsByRmCode(String rmCode);
}
