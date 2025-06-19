// File: InvestmentEntity.java
package com.mphasis.rmclient.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "investment_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "entity_name", length = 100)
    private String entityName;

    @Column(name = "sector", length = 100)
    private String sector;
}
