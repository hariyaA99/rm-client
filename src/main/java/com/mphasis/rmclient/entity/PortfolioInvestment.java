// File: PortfolioInvestment.java
package com.mphasis.rmclient.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "portfolio_investments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioInvestment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "investment_id")
    private Integer investmentId;

    @Column(name = "portfolio_id", nullable = false)
    private Integer portfolioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private InvestmentCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false)
    private InvestmentEntity entity;

    @Column(name = "amount_invested", precision = 15, scale = 2, nullable = false)
    private BigDecimal amountInvested;

    @Column(name = "current_value", precision = 15, scale = 2)
    private BigDecimal currentValue;

    @Column(name = "units_held", nullable = false)
    private Integer unitsHeld;

    @Column(name = "invested_on", nullable = false)
    private LocalDate investedOn;

    @Column(name = "remarks")
    private String remarks;

    // Correct mapping to RMMapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id", nullable = false)
    private RMMapping client;
}
