// Filename: SingleClientInvestmentSummaryController.java
package com.mphasis.rmclient.controller;

import com.mphasis.rmclient.dto.SingleClientInvestmentSummaryDTO;
import com.mphasis.rmclient.service.interfaces.SingleClientInvestmentSummaryService;
import com.mphasis.rmclient.service.interfaces.TokenValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http:localhost:4200")
public class SingleClientInvestmentSummaryController {

    @Autowired
    private SingleClientInvestmentSummaryService service;

    @Autowired
    private TokenValidatorService tokenValidatorService;

    private void validateRequest(HttpHeaders headers) {
        tokenValidatorService.validateToken(headers);
    }

    @GetMapping("/client/{clientId}/summary")
    public ResponseEntity<SingleClientInvestmentSummaryDTO> getSummary(
            @RequestHeader HttpHeaders headers,
            @PathVariable int clientId) {

        validateRequest(headers);

        SingleClientInvestmentSummaryDTO summary = service.getInvestmentSummary(clientId);
        if (summary == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(summary);
    }
}
