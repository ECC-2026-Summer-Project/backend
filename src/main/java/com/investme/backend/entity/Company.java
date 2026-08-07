package com.investme.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "company")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {

    @Id
    @Column(name = "company_id", length = 20)
    private String companyId; // 예: "005930"

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "sector", length = 50)
    private String sector;

    @Column(name = "current_price", nullable = false)
    private Long currentPrice;

    @Column(name = "trend", length = 20)
    private String trend; // 알고리즘에서 사용 (예: "UP", "DOWN", "FLAT")

    @Column(name = "volatility")
    private Double volatility;

    @Column(name = "dividend_yield")
    private Double dividendYield;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Builder
    public Company(String companyId, String companyName, String sector,
                   Long currentPrice, String trend, Double volatility,
                   Double dividendYield, LocalDateTime lastUpdated) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.sector = sector;
        this.currentPrice = currentPrice;
        this.trend = trend;
        this.volatility = volatility;
        this.dividendYield = dividendYield;
        this.lastUpdated = lastUpdated;
    }

    // 알고리즘이 가격을 갱신할 때 사용할 수 있는 편의 메서드
    public void updatePrice(Long newPrice, String newTrend, LocalDateTime updatedAt) {
        this.currentPrice = newPrice;
        this.trend = newTrend;
        this.lastUpdated = updatedAt;
    }
}