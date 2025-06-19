// File: RelationshipManagerServiceImpl.java
package com.mphasis.rmclient.service.implementations;

import com.mphasis.rmclient.entity.RelationshipManager;
import com.mphasis.rmclient.repository.RelationshipManagerRepository;
import com.mphasis.rmclient.service.interfaces.RelationshipManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RelationshipManagerServiceImpl implements RelationshipManagerService {

    @Autowired
    private RelationshipManagerRepository relationshipManagerRepository;

    @Override
    public Optional<String> getRmNameByCode(String rmCode) {
        Optional<RelationshipManager> rm = relationshipManagerRepository.findById(rmCode);
        return rm.map(RelationshipManager::getName);
    }

    @Override
    public Optional<String> getRmCodeByEmail(String email) {
        return relationshipManagerRepository.findRmCodeByEmail(email);
    }

    @Override
    public Optional<RelationshipManager> getRmByEmail(String email) {
        return relationshipManagerRepository.findByEmail(email);
    }
}
