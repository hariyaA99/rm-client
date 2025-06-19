package com.mphasis.rmclient.repository;

import com.mphasis.rmclient.entity.InvestmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentEntityRepository extends JpaRepository<InvestmentEntity, Integer> {

    @Query("SELECT DISTINCT ie.entityName FROM InvestmentEntity ie")
    List<String> findAllEntityNames();
}
