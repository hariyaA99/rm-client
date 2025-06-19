package com.mphasis.rmclient.service.interfaces;

import com.mphasis.rmclient.entity.RelationshipManager;
import java.util.Optional;

public interface RelationshipManagerService {
    Optional<String> getRmNameByCode(String rmCode);
    Optional<String> getRmCodeByEmail(String email);
    Optional<RelationshipManager> getRmByEmail(String email);
}