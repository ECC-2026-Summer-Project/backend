package com.investme.backend.service;

import com.investme.backend.domain.TradeHistory;
import com.investme.backend.dto.ReportCreateResponse;
import com.investme.backend.dto.ReportResponse;
import com.investme.backend.entity.Company;
import com.investme.backend.entity.PsychologyReport;
import com.investme.backend.entity.UserActionLog;
import com.investme.backend.entity.UserNewsEvent;
import com.investme.backend.entity.UserStock;
import com.investme.backend.repository.CompanyRepository;
import com.investme.backend.repository.PsychologyReportRepository;
import com.investme.backend.repository.TradeHistoryRepository;
import com.investme.backend.repository.UserActionLogRepository;
import com.investme.backend.repository.UserNewsEventRepository;
import com.investme.backend.repository.UserStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TradeHistoryRepository tradeHistoryRepository;
    private final UserActionLogRepository userActionLogRepository;
    private final UserNewsEventRepository userNewsEventRepository;
    private final UserStockRepository userStockRepository;
    private final CompanyRepository companyRepository;
    private final PsychologyReportRepository psychologyReportRepository;


    // =========================================================
    // POST /api/reports 에서 사용할 레포트 생성 응답
    // =========================================================

    @Transactional
    public ReportCreateResponse createReportResponse(Long userId) {

        PsychologyReport savedReport =
                createReport(userId);

        return new ReportCreateResponse(
                savedReport.getReportId(),
                savedReport.getCreatedAt()
        );
    }


    // =========================================================
    // 레포트 생성 및 DB 저장
    // =========================================================

    @Transactional
    public PsychologyReport createReport(Long userId) {

        // 1. 심리 점수 및 투자 유형 계산
        ReportScoreResult scoreResult =
                calculateReportScores(userId);


        // 2. 현재 보유 종목 조회
        List<UserStock> holdings =
                userStockRepository.findAllByUserId(userId);


        // 3. 현재 보유 종목의 총 매입 금액
        long totalPurchaseAmount =
                holdings.stream()
                        .mapToLong(UserStock::getTotalAmount)
                        .sum();


        // 4. 현재 보유 종목의 총 평가 금액
        long totalEvaluationAmount =
                holdings.stream()
                        .mapToLong(holding -> {

                            Company company =
                                    companyRepository
                                            .findById(
                                                    holding.getCompanyId()
                                            )
                                            .orElseThrow(() ->
                                                    new IllegalArgumentException(
                                                            "종목을 찾을 수 없습니다."
                                                    )
                                            );

                            return (long) holding.getQuantity()
                                    * company.getCurrentPrice();
                        })
                        .sum();


        // 5. 평가 손익
        long totalProfitLoss =
                totalEvaluationAmount
                        - totalPurchaseAmount;


        // 6. 전체 수익률
        double totalReturnRate =
                totalPurchaseAmount == 0
                        ? 0.0
                        : (double) totalProfitLoss
                        / totalPurchaseAmount
                        * 100;

        totalReturnRate =
                round(totalReturnRate);


        // 7. 전체 거래 조회
        List<TradeHistory> trades =
                tradeHistoryRepository
                        .findAllByUserId(userId);


        // 체결 완료된 거래만 사용
        List<TradeHistory> filledTrades =
                trades.stream()
                        .filter(trade ->
                                "FILLED".equalsIgnoreCase(
                                        trade.getStatus()
                                )
                        )
                        .toList();


        int totalTradeCount =
                filledTrades.size();


        // 8. 매수 횟수
        int buyCount =
                (int) filledTrades.stream()
                        .filter(trade ->
                                "BUY".equalsIgnoreCase(
                                        trade.getSide()
                                )
                        )
                        .count();


        // 9. 매도 횟수
        int sellCount =
                (int) filledTrades.stream()
                        .filter(trade ->
                                "SELL".equalsIgnoreCase(
                                        trade.getSide()
                                )
                        )
                        .count();


        // 10. 뉴스 열람 개수
        List<UserNewsEvent> newsEvents =
                userNewsEventRepository
                        .findAllByUserId(userId);

        int viewedNewsCount =
                (int) newsEvents.stream()
                        .filter(event ->
                                event.getDurationSeconds() != null
                        )
                        .count();


        // 11. AI 추천을 통해 실제 매수한 횟수
        int aiRecommendedPurchaseCount =
                calculateAiRecommendedPurchaseCount(
                        userId
                );


        // 12. BEST / WORST 종목 계산
        StockPerformanceResult stockPerformance =
                calculateStockPerformance(
                        holdings
                );


        // 13. 투자 유형 설명
        String description =
                getInvestmentTypeDescription(
                        scoreResult.investmentType
                );


        // 14. 종합 피드백
        String feedback =
                getFeedback(
                        scoreResult.investmentType
                );


        // 15. PsychologyReport 생성
        PsychologyReport report =
                new PsychologyReport(
                        userId,

                        scoreResult.investmentType,
                        scoreResult.investmentTypeScore,
                        description,

                        scoreResult.recommendationFollowingScore,
                        scoreResult.informationCautionScore,

                        scoreResult.aiRecommendationScore,
                        scoreResult.surgingStockScore,
                        scoreResult.newsInformationScore,

                        totalPurchaseAmount,
                        totalEvaluationAmount,
                        totalProfitLoss,
                        totalReturnRate,
                        totalTradeCount,

                        buyCount,
                        sellCount,
                        viewedNewsCount,
                        aiRecommendedPurchaseCount,

                        stockPerformance.bestStockId,
                        stockPerformance.bestStockName,
                        stockPerformance.bestReturnRate,

                        stockPerformance.worstStockId,
                        stockPerformance.worstStockName,
                        stockPerformance.worstReturnRate,

                        feedback
                );


        // 16. DB 저장
        return psychologyReportRepository.save(
                report
        );
    }


    // =========================================================
    // GET /api/reports/{reportId} 에서 사용할 레포트 조회
    // =========================================================

    @Transactional(readOnly = true)
    public ReportResponse getReport(
            Long reportId,
            Long userId
    ) {

        PsychologyReport report =
                psychologyReportRepository
                        .findByReportIdAndUserId(
                                reportId,
                                userId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "생성된 레포트가 없습니다."
                                )
                        );


        ReportResponse.InvestmentStyle investmentStyle =
                new ReportResponse.InvestmentStyle(
                        report.getInvestmentType(),
                        report.getInvestmentTypeScore(),
                        report.getDescription()
                );


        ReportResponse.InvestmentSummary investmentSummary =
                new ReportResponse.InvestmentSummary(
                        report.getTotalReturnRate(),
                        report.getTotalPurchaseAmount(),
                        report.getTotalEvaluationAmount(),
                        report.getTotalProfitLoss(),
                        report.getTotalTradeCount()
                );


        ReportResponse.TriggerSensitivity triggerSensitivity =
                new ReportResponse.TriggerSensitivity(
                        report.getAiRecommendationScore(),
                        report.getSurgingStockScore(),
                        report.getNewsInformationScore()
                );


        ReportResponse.BehaviorAnalysis behaviorAnalysis =
                new ReportResponse.BehaviorAnalysis(
                        report.getBuyCount(),
                        report.getSellCount(),
                        report.getViewedNewsCount(),
                        report.getAiRecommendedPurchaseCount()
                );


        ReportResponse.StockPerformance bestPerformingStock =
                new ReportResponse.StockPerformance(
                        report.getBestStockId(),
                        report.getBestStockName(),
                        report.getBestStockReturnRate()
                );


        ReportResponse.StockPerformance worstPerformingStock =
                new ReportResponse.StockPerformance(
                        report.getWorstStockId(),
                        report.getWorstStockName(),
                        report.getWorstStockReturnRate()
                );


        return new ReportResponse(
                report.getReportId(),
                report.getCreatedAt(),

                investmentStyle,
                investmentSummary,
                triggerSensitivity,
                behaviorAnalysis,

                bestPerformingStock,
                worstPerformingStock,

                report.getFeedback()
        );
    }


    // =========================================================
    // 레포트 심리 점수 전체 계산
    // =========================================================

    private ReportScoreResult calculateReportScores(
            Long userId
    ) {

        double aiScore =
                calculateAiRecommendationScore(
                        userId
                );

        double surgingScore =
                calculateSurgingStockScore(
                        userId
                );

        double newsScore =
                calculateNewsInformationScore(
                        userId
                );


        // A. 추천 추종도
        double recommendationFollowingScore =
                round(
                        (aiScore + surgingScore)
                                / 2.0
                );


        // B. 정보 신중도
        double informationCautionScore =
                newsScore;


        InvestmentTypeResult investmentType =
                determineInvestmentType(
                        recommendationFollowingScore,
                        informationCautionScore
                );


        return new ReportScoreResult(
                aiScore,
                surgingScore,
                newsScore,
                recommendationFollowingScore,
                informationCautionScore,
                investmentType.getType(),
                investmentType.getScore()
        );
    }


    // =========================================================
    // AI 추천 반응도
    // =========================================================

    private double calculateAiRecommendationScore(
            Long userId
    ) {

        List<UserActionLog> aiEvents =
                userActionLogRepository
                        .findAllByUserIdAndTargetType(
                                userId,
                                "AI_RECOMMENDATION"
                        );


        if (aiEvents.isEmpty()) {
            return 0.0;
        }


        List<TradeHistory> trades =
                tradeHistoryRepository
                        .findAllByUserIdAndTriggeredByEventIdIsNotNull(
                                userId
                        );


        long acceptedCount =
                trades.stream()

                        .filter(trade ->
                                "FILLED".equalsIgnoreCase(
                                        trade.getStatus()
                                )
                        )

                        .filter(trade ->
                                "BUY".equalsIgnoreCase(
                                        trade.getSide()
                                )
                        )

                        .filter(trade ->
                                aiEvents.stream()
                                        .anyMatch(event ->
                                                event.getActionId()
                                                        .equals(
                                                                trade.getTriggeredByEventId()
                                                        )
                                        )
                        )

                        .count();


        double score =
                (double) acceptedCount
                        / aiEvents.size()
                        * 100;


        return round(
                clamp(score)
        );
    }


    // =========================================================
    // 급등주 추천 반응도
    // =========================================================

    private double calculateSurgingStockScore(
            Long userId
    ) {

        List<UserActionLog> surgeEvents =
                userActionLogRepository
                        .findAllByUserIdAndTargetType(
                                userId,
                                "SURGING_STOCK"
                        );


        if (surgeEvents.isEmpty()) {
            return 0.0;
        }


        List<TradeHistory> trades =
                tradeHistoryRepository
                        .findAllByUserIdAndTriggeredByEventIdIsNotNull(
                                userId
                        );


        long acceptedCount =
                trades.stream()

                        .filter(trade ->
                                "FILLED".equalsIgnoreCase(
                                        trade.getStatus()
                                )
                        )

                        .filter(trade ->
                                "BUY".equalsIgnoreCase(
                                        trade.getSide()
                                )
                        )

                        .filter(trade ->
                                surgeEvents.stream()
                                        .anyMatch(event ->
                                                event.getActionId()
                                                        .equals(
                                                                trade.getTriggeredByEventId()
                                                        )
                                        )
                        )

                        .count();


        double score =
                (double) acceptedCount
                        / surgeEvents.size()
                        * 100;


        return round(
                clamp(score)
        );
    }


    // =========================================================
    // 뉴스 정보 확인도
    // =========================================================

    private double calculateNewsInformationScore(
            Long userId
    ) {

        List<UserNewsEvent> newsEvents =
                userNewsEventRepository
                        .findAllByUserId(
                                userId
                        );


        long totalDurationSeconds =
                newsEvents.stream()

                        .filter(event ->
                                event.getDurationSeconds() != null
                        )

                        .mapToLong(
                                UserNewsEvent::getDurationSeconds
                        )

                        .sum();


        // 기준 시간 = 1시간(3600초)
        double score =
                Math.min(
                        (double) totalDurationSeconds
                                / 3600.0,
                        1.0
                ) * 100;


        return round(
                clamp(score)
        );
    }


    // =========================================================
    // AI 추천 실제 매수 횟수
    // =========================================================

    private int calculateAiRecommendedPurchaseCount(
            Long userId
    ) {

        List<UserActionLog> aiEvents =
                userActionLogRepository
                        .findAllByUserIdAndTargetType(
                                userId,
                                "AI_RECOMMENDATION"
                        );


        List<TradeHistory> trades =
                tradeHistoryRepository
                        .findAllByUserIdAndTriggeredByEventIdIsNotNull(
                                userId
                        );


        return (int) trades.stream()

                .filter(trade ->
                        "FILLED".equalsIgnoreCase(
                                trade.getStatus()
                        )
                )

                .filter(trade ->
                        "BUY".equalsIgnoreCase(
                                trade.getSide()
                        )
                )

                .filter(trade ->
                        aiEvents.stream()
                                .anyMatch(event ->
                                        event.getActionId()
                                                .equals(
                                                        trade.getTriggeredByEventId()
                                                )
                                )
                )

                .count();
    }


    // =========================================================
    // BEST / WORST 종목 계산
    // =========================================================

    private StockPerformanceResult calculateStockPerformance(
            List<UserStock> holdings
    ) {

        String bestStockId = null;
        String bestStockName = null;
        double bestReturnRate =
                Double.NEGATIVE_INFINITY;


        String worstStockId = null;
        String worstStockName = null;
        double worstReturnRate =
                Double.POSITIVE_INFINITY;


        for (UserStock holding : holdings) {

            Company company =
                    companyRepository
                            .findById(
                                    holding.getCompanyId()
                            )
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "종목을 찾을 수 없습니다."
                                    )
                            );


            long purchaseAmount =
                    holding.getTotalAmount();


            long evaluationAmount =
                    (long) holding.getQuantity()
                            * company.getCurrentPrice();


            double returnRate =
                    purchaseAmount == 0
                            ? 0.0
                            : (double) (
                            evaluationAmount
                                    - purchaseAmount
                    )
                            / purchaseAmount
                            * 100;


            returnRate =
                    round(returnRate);


            if (returnRate > bestReturnRate) {

                bestReturnRate =
                        returnRate;

                bestStockId =
                        company.getCompanyId();

                bestStockName =
                        company.getCompanyName();
            }


            if (returnRate < worstReturnRate) {

                worstReturnRate =
                        returnRate;

                worstStockId =
                        company.getCompanyId();

                worstStockName =
                        company.getCompanyName();
            }
        }


        if (holdings.isEmpty()) {

            bestReturnRate = 0.0;
            worstReturnRate = 0.0;
        }


        return new StockPerformanceResult(
                bestStockId,
                bestStockName,
                bestReturnRate,

                worstStockId,
                worstStockName,
                worstReturnRate
        );
    }


    // =========================================================
    // 6가지 투자 유형 판정
    // =========================================================

    private InvestmentTypeResult determineInvestmentType(
            double recommendationFollowingScore,
            double informationCautionScore
    ) {

        double crowdPsychologyScore =
                calculateTypeScore(
                        recommendationFollowingScore,
                        informationCautionScore,
                        0.9,
                        -0.4
                );


        double followBuyScore =
                calculateTypeScore(
                        recommendationFollowingScore,
                        informationCautionScore,
                        0.7,
                        0.1
                );


        double informationAnalysisScore =
                calculateTypeScore(
                        recommendationFollowingScore,
                        informationCautionScore,
                        -0.5,
                        0.9
                );


        double cautiousFollowerScore =
                calculateTypeScore(
                        recommendationFollowingScore,
                        informationCautionScore,
                        0.5,
                        0.6
                );


        double independentInvestorScore =
                calculateTypeScore(
                        recommendationFollowingScore,
                        informationCautionScore,
                        -0.6,
                        -0.5
                );


        double observerScore =
                calculateTypeScore(
                        recommendationFollowingScore,
                        informationCautionScore,
                        -0.2,
                        0.2
                );


        String selectedType =
                "군중심리형";

        double maxScore =
                crowdPsychologyScore;


        if (followBuyScore > maxScore) {

            selectedType =
                    "추종매수형";

            maxScore =
                    followBuyScore;
        }


        if (informationAnalysisScore > maxScore) {

            selectedType =
                    "정보분석형";

            maxScore =
                    informationAnalysisScore;
        }


        if (cautiousFollowerScore > maxScore) {

            selectedType =
                    "신중추종형";

            maxScore =
                    cautiousFollowerScore;
        }


        if (independentInvestorScore > maxScore) {

            selectedType =
                    "독립투자형";

            maxScore =
                    independentInvestorScore;
        }


        if (observerScore > maxScore) {

            selectedType =
                    "관망형";

            maxScore =
                    observerScore;
        }


        return new InvestmentTypeResult(
                selectedType,
                round(maxScore)
        );
    }


    // =========================================================
    // 유형 점수 공식
    // =========================================================

    private double calculateTypeScore(
            double recommendationFollowingScore,
            double informationCautionScore,
            double recommendationWeight,
            double cautionWeight
    ) {

        double score =
                50
                        + recommendationWeight
                        * (
                        recommendationFollowingScore
                                - 50
                )
                        + cautionWeight
                        * (
                        informationCautionScore
                                - 50
                );


        return clamp(score);
    }


    // =========================================================
    // 투자 유형 설명
    // =========================================================

    private String getInvestmentTypeDescription(
            String type
    ) {

        return switch (type) {

            case "군중심리형" ->
                    "추천 종목에 적극적으로 반응하고 비교적 빠르게 투자 판단을 내리는 성향을 보였습니다.";

            case "추종매수형" ->
                    "추천된 종목을 참고하여 매수 판단을 내리는 경향이 높은 투자 성향을 보였습니다.";

            case "정보분석형" ->
                    "추천을 그대로 따르기보다 충분한 정보를 확인한 뒤 판단하는 성향을 보였습니다.";

            case "신중추종형" ->
                    "추천을 참고하면서도 정보를 충분히 확인한 뒤 투자하는 성향을 보였습니다.";

            case "독립투자형" ->
                    "외부 추천이나 뉴스에 크게 의존하지 않고 독립적으로 투자 판단을 내리는 성향을 보였습니다.";

            case "관망형" ->
                    "추천이나 정보에 즉시 반응하기보다는 상황을 지켜보는 성향을 보였습니다.";

            default ->
                    "투자 행동을 기반으로 투자 성향을 분석했습니다.";
        };
    }


    // =========================================================
    // 종합 피드백
    // =========================================================

    private String getFeedback(
            String type
    ) {

        return switch (type) {

            case "군중심리형" ->
                    "추천 종목에 민감하게 반응하는 편입니다. 투자 전 뉴스나 기업 정보를 한 번 더 확인해 보세요.";

            case "추종매수형" ->
                    "추천 정보를 적극적으로 활용하고 있습니다. 추천 이유와 실제 기업 정보를 함께 확인하면 더 신중한 판단에 도움이 됩니다.";

            case "정보분석형" ->
                    "정보를 충분히 확인한 후 투자하는 성향이 강합니다. 분석에 너무 많은 시간을 쓰기보다 투자 기준을 미리 정해보는 것도 좋습니다.";

            case "신중추종형" ->
                    "추천과 정보 분석을 균형 있게 활용하고 있습니다. 현재의 신중한 판단 방식을 유지해 보세요.";

            case "독립투자형" ->
                    "외부 자극에 크게 흔들리지 않는 편입니다. 다만 중요한 뉴스나 시장 정보까지 놓치지 않도록 확인하는 습관도 필요합니다.";

            case "관망형" ->
                    "투자 결정을 비교적 천천히 내리는 편입니다. 자신만의 매수·매도 기준을 세우면 결정에 도움이 될 수 있습니다.";

            default ->
                    "투자 전 충분한 정보를 확인하고 자신만의 기준에 따라 판단해 보세요.";
        };
    }


    // =========================================================
    // 공통 유틸
    // =========================================================

    private double clamp(
            double score
    ) {

        return Math.max(
                0.0,
                Math.min(
                        100.0,
                        score
                )
        );
    }


    private double round(
            double value
    ) {

        return Math.round(
                value * 100.0
        ) / 100.0;
    }


    // =========================================================
    // 투자 유형 결과
    // =========================================================

    private static class InvestmentTypeResult {

        private final String type;
        private final double score;


        public InvestmentTypeResult(
                String type,
                double score
        ) {

            this.type =
                    type;

            this.score =
                    score;
        }


        public String getType() {

            return type;
        }


        public double getScore() {

            return score;
        }
    }


    // =========================================================
    // 심리 점수 결과
    // =========================================================

    private static class ReportScoreResult {

        private final double aiRecommendationScore;
        private final double surgingStockScore;
        private final double newsInformationScore;

        private final double recommendationFollowingScore;
        private final double informationCautionScore;

        private final String investmentType;
        private final double investmentTypeScore;


        public ReportScoreResult(
                double aiRecommendationScore,
                double surgingStockScore,
                double newsInformationScore,
                double recommendationFollowingScore,
                double informationCautionScore,
                String investmentType,
                double investmentTypeScore
        ) {

            this.aiRecommendationScore =
                    aiRecommendationScore;

            this.surgingStockScore =
                    surgingStockScore;

            this.newsInformationScore =
                    newsInformationScore;

            this.recommendationFollowingScore =
                    recommendationFollowingScore;

            this.informationCautionScore =
                    informationCautionScore;

            this.investmentType =
                    investmentType;

            this.investmentTypeScore =
                    investmentTypeScore;
        }
    }


    // =========================================================
    // BEST / WORST 계산 결과
    // =========================================================

    private static class StockPerformanceResult {

        private final String bestStockId;
        private final String bestStockName;
        private final double bestReturnRate;

        private final String worstStockId;
        private final String worstStockName;
        private final double worstReturnRate;


        public StockPerformanceResult(
                String bestStockId,
                String bestStockName,
                double bestReturnRate,
                String worstStockId,
                String worstStockName,
                double worstReturnRate
        ) {

            this.bestStockId =
                    bestStockId;

            this.bestStockName =
                    bestStockName;

            this.bestReturnRate =
                    bestReturnRate;

            this.worstStockId =
                    worstStockId;

            this.worstStockName =
                    worstStockName;

            this.worstReturnRate =
                    worstReturnRate;
        }
    }
}