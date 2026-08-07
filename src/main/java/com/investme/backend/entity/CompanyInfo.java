package com.investme.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "company_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyInfo {

    @Id
    @Column(name = "company_id", length = 20)
    private String companyId; // Company와 동일한 PK를 공유 (FK 겸용)

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "market", length = 20)
    private String market; // 예: "KOSPI", "KOSDAQ"

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "listed_date")
    private LocalDate listedDate;

    @Column(name = "ceo", length = 50)
    private String ceo;

    @Column(name = "employees")
    private Integer employees;

    @Column(name = "is_ai_recommended")
    private Boolean isAiRecommended;

    @Column(name = "market_cap")
    private Long marketCap; // 고정값 (예: 427000000000000)

    @Column(name = "per")
    private Double per; // 고정값 (예: 15.8)

    @Builder
    public CompanyInfo(Company company, String market, String description,
                       LocalDate listedDate, String ceo, Integer employees,
                       Boolean isAiRecommended, Long marketCap, Double per) {
        this.company = company;
        this.market = market;
        this.description = description;
        this.listedDate = listedDate;
        this.ceo = ceo;
        this.employees = employees;
        this.isAiRecommended = isAiRecommended;
        this.marketCap = marketCap;
        this.per = per;
    }
}