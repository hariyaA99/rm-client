package com.mphasis.rmclient.repository;

import com.mphasis.rmclient.entity.InvestmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentCategoryRepository extends JpaRepository<InvestmentCategory, Integer> {
}

