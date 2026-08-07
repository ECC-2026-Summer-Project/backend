package com.investme.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dividend")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dividend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dividend_id")
    private Long dividendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "amount_per_share", nullable = false)
    private Integer amountPerShare;

    @Column(name = "yield_rate")
    private Double yieldRate;

    @Builder
    public Dividend(Company company, Integer year, Integer amountPerShare, Double yieldRate) {
        this.company = company;
        this.year = year;
        this.amountPerShare = amountPerShare;
        this.yieldRate = yieldRate;
    }
}
