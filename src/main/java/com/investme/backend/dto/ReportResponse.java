package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReportResponse {

    private Long reportId;
    private LocalDateTime createdAt;

    private InvestmentStyle investmentStyle;
    private InvestmentSummary investmentSummary;
    private TriggerSensitivity triggerSensitivity;
    private BehaviorAnalysis behaviorAnalysis;

    private StockPerformance bestPerformingStock;
    private StockPerformance worstPerformingStock;

    private String feedback;


    @Getter
    @AllArgsConstructor
    public static class InvestmentStyle {

        private String type;
        private Double score;
        private String description;
    }


    @Getter
    @AllArgsConstructor
    public static class InvestmentSummary {

        private Double totalReturnRate;
        private Long totalPurchaseAmount;
        private Long totalEvaluationAmount;
        private Long totalProfitLoss;
        private Integer totalTradeCount;
    }


    @Getter
    @AllArgsConstructor
    public static class TriggerSensitivity {

        private Double aiRecommendationScore;
        private Double surgingStockScore;
        private Double newsInformationScore;
    }


    @Getter
    @AllArgsConstructor
    public static class BehaviorAnalysis {

        private Integer buyCount;
        private Integer sellCount;
        private Integer viewedNewsCount;
        private Integer aiRecommendedPurchaseCount;
    }


    @Getter
    @AllArgsConstructor
    public static class StockPerformance {

        private String stockId;
        private String stockName;
        private Double returnRate;
    }
}
