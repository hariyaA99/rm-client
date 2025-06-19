// File: PortfolioInvestmentRepository.java
package com.mphasis.rmclient.repository;

import com.mphasis.rmclient.dto.ClientMappingSummaryDTO;
import com.mphasis.rmclient.entity.PortfolioInvestment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PortfolioInvestmentRepository extends JpaRepository<PortfolioInvestment, Integer> {

    // 1. Summary by RM code with clientId
    @Query("SELECT new com.mphasis.rmclient.dto.ClientMappingSummaryDTO(i.client.clientId, i.client.name, i.client.email, SUM(i.amountInvested)) " +
            "FROM PortfolioInvestment i " +
            "WHERE i.client.mappedRmCode = :rmCode " +
            "GROUP BY i.client.clientId, i.client.name, i.client.email")
    List<ClientMappingSummaryDTO> findInvestmentsByRmCode(@Param("rmCode") String rmCode);

    // 2. Fetch by client ID
    List<PortfolioInvestment> findByClient_ClientId(int clientId);

    // 3. Get all portfolio investments by RM code
    @Query("SELECT pi FROM PortfolioInvestment pi WHERE pi.client.clientId IN " +
            "(SELECT rm.clientId FROM RMMapping rm WHERE rm.mappedRmCode = :rmCode)")
    List<PortfolioInvestment> findByRmCode(@Param("rmCode") String rmCode);

    // 4. Sector-wise investment aggregation
    @Query("SELECT ie.sector, SUM(pi.amountInvested) FROM PortfolioInvestment pi " +
            "JOIN pi.entity ie " +
            "WHERE pi.client.clientId IN (SELECT rm.clientId FROM RMMapping rm WHERE rm.mappedRmCode = :rmCode) " +
            "GROUP BY ie.sector")
    List<Object[]> findSectorWiseInvestmentByRmCode(@Param("rmCode") String rmCode);

    // 5. Total investment by RM code
    @Query("SELECT SUM(pi.amountInvested) FROM PortfolioInvestment pi " +
            "WHERE pi.client.clientId IN (SELECT rm.clientId FROM RMMapping rm WHERE rm.mappedRmCode = :rmCode)")
    BigDecimal findTotalInvestmentByRmCode(@Param("rmCode") String rmCode);

    // 6. Total units held by RM code
    @Query("SELECT SUM(pi.unitsHeld) FROM PortfolioInvestment pi " +
            "WHERE pi.client.clientId IN (SELECT rm.clientId FROM RMMapping rm WHERE rm.mappedRmCode = :rmCode)")
    Integer findTotalUnitsHeldByRmCode(@Param("rmCode") String rmCode);

    // 7. Get portfolio with entity names
    @Query("SELECT pi, ie.entityName FROM PortfolioInvestment pi " +
            "JOIN pi.entity ie " +
            "WHERE pi.client.clientId IN (SELECT rm.clientId FROM RMMapping rm WHERE rm.mappedRmCode = :rmCode)")
    List<Object[]> findPortfolioWithEntityNamesByRmCode(@Param("rmCode") String rmCode);

    // 8. Top 5 clients with highest amountInvested
    @Query(value = """
    SELECT pi.client_id,
           rm.name as client_name,
           rm.email as client_email,
           SUM(pi.amount_invested) AS total_invested,
           SUM(pi.current_value) AS total_current_value,
           CASE
               WHEN SUM(pi.amount_invested) > 0
               THEN ((SUM(pi.current_value) - SUM(pi.amount_invested)) / SUM(pi.amount_invested)) * 100
               ELSE 0
           END AS profit_percentage
    FROM portfolio_investments pi
    JOIN rm_mapping rm ON pi.client_id = rm.client_id
    WHERE rm.mapped_rm_code = :rmCode
    GROUP BY pi.client_id, rm.name, rm.email
    ORDER BY total_invested DESC
    LIMIT 5
    """, nativeQuery = true)
    List<Object[]> findTop5ClientsByRmCode(@Param("rmCode") String rmCode);

    // 9. Bottom 5 clients with lowest amountInvested
    @Query(value = """
    SELECT pi.client_id,
           rm.name as client_name,
           rm.email as client_email,
           SUM(pi.amount_invested) AS total_invested,
           SUM(pi.current_value) AS total_current_value,
           CASE
               WHEN SUM(pi.amount_invested) > 0
               THEN ((SUM(pi.current_value) - SUM(pi.amount_invested)) / SUM(pi.amount_invested)) * 100
               ELSE 0
           END AS profit_percentage
    FROM portfolio_investments pi
    JOIN rm_mapping rm ON pi.client_id = rm.client_id
    WHERE rm.mapped_rm_code = :rmCode
    GROUP BY pi.client_id, rm.name, rm.email
    ORDER BY total_invested ASC
    LIMIT 5
    """, nativeQuery = true)
    List<Object[]> findBottom5ClientsByRmCode(@Param("rmCode") String rmCode);

    // 10. Client Investment Summary
    @Query("SELECT r.name, r.email, SUM(p.amountInvested), SUM(p.currentValue), SUM(p.unitsHeld) " +
            "FROM PortfolioInvestment p JOIN RMMapping r ON p.client.clientId = r.clientId " +
            "WHERE r.clientId = :clientId")
    Object getClientInvestmentSummary(@Param("clientId") Long clientId);
}
