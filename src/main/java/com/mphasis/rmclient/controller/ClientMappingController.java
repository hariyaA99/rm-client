// File: ClientMappingController.java
package com.mphasis.rmclient.controller;

import com.mphasis.rmclient.dto.ClientMappingSummaryDTO;
import com.mphasis.rmclient.service.interfaces.ClientMappingService;
import com.mphasis.rmclient.service.interfaces.TokenValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client-mapping")
@CrossOrigin(origins = "*")
public class ClientMappingController {

    @Autowired
    private ClientMappingService clientMappingService;

    @Autowired
    private TokenValidatorService tokenValidatorService;

    // Helper to extract and validate RM code from token
    private String getRmCodeFromHeaders(HttpHeaders headers) {
        tokenValidatorService.validateToken(headers);
        return tokenValidatorService.getRmCodeFromSession(headers);
    }

    @GetMapping("/rm")
    public List<ClientMappingSummaryDTO> getClientsByRmCode(@RequestHeader HttpHeaders headers) {
        String rmCode = getRmCodeFromHeaders(headers);
        return clientMappingService.getClientsByRmCode(rmCode);
    }
}
