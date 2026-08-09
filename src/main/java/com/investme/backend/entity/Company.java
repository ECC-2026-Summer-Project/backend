package com.investme.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Company")
public class Company {

    @Id
    @Column(name = "company_id")
    private String companyId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "sector")
    private String sector;

    @Column(name = "current_price", nullable = false)
    private Integer currentPrice;

    @Column(name = "base_price", nullable = false)
    private Integer basePrice;
    
    @Column(name = "trend")
    private String trend;

    @Column(name = "volatility")
    private Float volatility;

    @Column(name = "dividend_yield")
    private Float dividendYield;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public void updatePrice(Integer newPrice) {
        this.currentPrice = newPrice;
        this.lastUpdated = LocalDateTime.now();
      }
}