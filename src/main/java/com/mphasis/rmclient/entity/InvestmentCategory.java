// File: InvestmentCategory.java
package com.mphasis.rmclient.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "investment_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name", length = 50)
    private String categoryName;
}
