package com.mphasis.rmclient.feign;

import com.mphasis.rmclient.dto.TokenValidationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "onboarding-service", url = "http://localhost:8082/api")
public interface TokenValidationClient {

    @PostMapping("/validate-token")
    ResponseEntity<TokenValidationResponseDto> validateToken(
            @RequestHeader("Username") String username,
            @RequestHeader("Authorization") String token
    );
}
