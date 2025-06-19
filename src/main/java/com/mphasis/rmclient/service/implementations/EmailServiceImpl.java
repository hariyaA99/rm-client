package com.mphasis.rmclient.service.implementations;

import com.mphasis.rmclient.repository.EmailRepository;
import com.mphasis.rmclient.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;

    @Autowired
    public EmailServiceImpl(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public long getTotalLeads() {
        return emailRepository.countByEmailType("leads");
    }
}
