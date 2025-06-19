// File: RMMappingRepository.java
package com.mphasis.rmclient.repository;

import com.mphasis.rmclient.entity.RMMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RMMappingRepository extends JpaRepository<RMMapping, Integer> {

    // Get all clients mapped to a specific RM code
    List<RMMapping> findByMappedRmCode(String mappedRmCode);

    // Count how many clients are mapped to a specific RM code
    @Query("SELECT COUNT(rm) FROM RMMapping rm WHERE rm.mappedRmCode = :rmCode")
    Integer countClientsByRmCode(@Param("rmCode") String rmCode);

    // Get only the client IDs of clients mapped to a specific RM
    @Query("SELECT rm.clientId FROM RMMapping rm WHERE rm.mappedRmCode = :rmCode")
    List<Integer> findClientIdsByRmCode(@Param("rmCode") String rmCode);
}
