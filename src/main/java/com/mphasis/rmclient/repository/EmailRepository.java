package com.mphasis.rmclient.repository;

import com.mphasis.rmclient.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

    long countByEmailType(String emailType);

}
