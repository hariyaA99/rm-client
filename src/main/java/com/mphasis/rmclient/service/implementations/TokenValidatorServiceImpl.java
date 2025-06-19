// File: TokenValidatorServiceImpl.java
package com.mphasis.rmclient.service.implementations;

import com.mphasis.rmclient.dto.TokenValidationResponseDto;
import com.mphasis.rmclient.feign.TokenValidationClient;
import com.mphasis.rmclient.service.interfaces.TokenValidatorService;
import com.mphasis.rmclient.service.interfaces.RelationshipManagerService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TokenValidatorServiceImpl implements TokenValidatorService {

    @Autowired
    private TokenValidationClient tokenValidationClient;

    @Autowired
    private RelationshipManagerService relationshipManagerService;

    @Override
    public void validateToken(HttpHeaders headers) {
        String username = headers.getFirst("Username");
        String token = headers.getFirst("Authorization");

        if (username == null || token == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Missing token or username");
        }

        try {
            TokenValidationResponseDto response = tokenValidationClient
                    .validateToken(username, token)
                    .getBody();

            if (response == null || !response.isValid()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token is invalid or expired");
            }
        } catch (FeignException.Forbidden ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Session has expired");
        } catch (FeignException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Token validation service unavailable");
        }
    }

    @Override
    public String getRmCodeFromSession(HttpHeaders headers) {
        String username = headers.getFirst("Username");

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Missing username in headers");
        }

        return relationshipManagerService.getRmCodeByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No relationship manager found for user: " + username));
    }
}
