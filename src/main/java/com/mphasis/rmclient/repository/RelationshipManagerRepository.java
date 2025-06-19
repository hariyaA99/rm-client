// File: RelationshipManagerRepository.java
package com.mphasis.rmclient.repository;

import com.mphasis.rmclient.entity.RelationshipManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RelationshipManagerRepository extends JpaRepository<RelationshipManager, String> {

    // Find RM code by email (username)
    @Query("SELECT rm.rmCode FROM RelationshipManager rm WHERE rm.email = :email")
    Optional<String> findRmCodeByEmail(@Param("email") String email);

    // Find complete RM details by email
    @Query("SELECT rm FROM RelationshipManager rm WHERE rm.email = :email")
    Optional<RelationshipManager> findByEmail(@Param("email") String email);
}
