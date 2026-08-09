package com.investme.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "psychology_report")
public class PsychologyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "user_id", nullable = false)
    private Long userId;


    // =========================
    // 투자 유형
    // =========================

    @Column(name = "investment_type", nullable = false)
    private String investmentType;

    @Column(name = "investment_type_score", nullable = false)
    private Double investmentTypeScore;

    @Column(name = "description", length = 1000)
    private String description;


    // =========================
    // 유형 계산용 핵심 지표
    // =========================

    // A. 추천 추종도
    @Column(name = "recommendation_following_score")
    private Double recommendationFollowingScore;

    // B. 정보 신중도
    @Column(name = "information_caution_score")
    private Double informationCautionScore;


    // =========================
    // 심리 트리거 민감도
    // =========================

    @Column(name = "ai_recommendation_score")
    private Double aiRecommendationScore;

    @Column(name = "surging_stock_score")
    private Double surgingStockScore;

    @Column(name = "news_information_score")
    private Double newsInformationScore;


    // =========================
    // 투자 결과 요약
    // =========================

    @Column(name = "total_purchase_amount")
    private Long totalPurchaseAmount;

    @Column(name = "total_evaluation_amount")
    private Long totalEvaluationAmount;

    @Column(name = "total_profit_loss")
    private Long totalProfitLoss;

    @Column(name = "total_return_rate")
    private Double totalReturnRate;

    @Column(name = "total_trade_count")
    private Integer totalTradeCount;


    // =========================
    // 투자 행동 분석
    // =========================

    @Column(name = "buy_count")
    private Integer buyCount;

    @Column(name = "sell_count")
    private Integer sellCount;

    @Column(name = "viewed_news_count")
    private Integer viewedNewsCount;

    @Column(name = "ai_recommended_purchase_count")
    private Integer aiRecommendedPurchaseCount;


    // =========================
    // BEST 종목
    // =========================

    @Column(name = "best_stock_id")
    private String bestStockId;

    @Column(name = "best_stock_name")
    private String bestStockName;

    @Column(name = "best_stock_return_rate")
    private Double bestStockReturnRate;


    // =========================
    // WORST 종목
    // =========================

    @Column(name = "worst_stock_id")
    private String worstStockId;

    @Column(name = "worst_stock_name")
    private String worstStockName;

    @Column(name = "worst_stock_return_rate")
    private Double worstStockReturnRate;


    // =========================
    // 종합 피드백
    // =========================

    @Column(name = "feedback", length = 2000)
    private String feedback;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    public PsychologyReport(
            Long userId,

            String investmentType,
            Double investmentTypeScore,
            String description,

            Double recommendationFollowingScore,
            Double informationCautionScore,

            Double aiRecommendationScore,
            Double surgingStockScore,
            Double newsInformationScore,

            Long totalPurchaseAmount,
            Long totalEvaluationAmount,
            Long totalProfitLoss,
            Double totalReturnRate,
            Integer totalTradeCount,

            Integer buyCount,
            Integer sellCount,
            Integer viewedNewsCount,
            Integer aiRecommendedPurchaseCount,

            String bestStockId,
            String bestStockName,
            Double bestStockReturnRate,

            String worstStockId,
            String worstStockName,
            Double worstStockReturnRate,

            String feedback
    ) {
        this.userId = userId;

        this.investmentType = investmentType;
        this.investmentTypeScore = investmentTypeScore;
        this.description = description;

        this.recommendationFollowingScore =
                recommendationFollowingScore;
        this.informationCautionScore =
                informationCautionScore;

        this.aiRecommendationScore =
                aiRecommendationScore;
        this.surgingStockScore =
                surgingStockScore;
        this.newsInformationScore =
                newsInformationScore;

        this.totalPurchaseAmount =
                totalPurchaseAmount;
        this.totalEvaluationAmount =
                totalEvaluationAmount;
        this.totalProfitLoss =
                totalProfitLoss;
        this.totalReturnRate =
                totalReturnRate;
        this.totalTradeCount =
                totalTradeCount;

        this.buyCount = buyCount;
        this.sellCount = sellCount;
        this.viewedNewsCount =
                viewedNewsCount;
        this.aiRecommendedPurchaseCount =
                aiRecommendedPurchaseCount;

        this.bestStockId =
                bestStockId;
        this.bestStockName =
                bestStockName;
        this.bestStockReturnRate =
                bestStockReturnRate;

        this.worstStockId =
                worstStockId;
        this.worstStockName =
                worstStockName;
        this.worstStockReturnRate =
                worstStockReturnRate;

        this.feedback = feedback;

        this.createdAt = LocalDateTime.now();
    }
}