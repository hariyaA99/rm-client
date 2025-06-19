package com.mphasis.rmclient.service.interfaces;

import org.springframework.http.HttpHeaders;

public interface TokenValidatorService {
    void validateToken(HttpHeaders headers);
    String getRmCodeFromSession(HttpHeaders headers);
}