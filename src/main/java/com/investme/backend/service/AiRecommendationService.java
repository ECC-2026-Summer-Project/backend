package com.investme.backend.service;

import com.investme.backend.domain.Stock;
import com.investme.backend.dto.SurgingStockResponse;
import com.investme.backend.entity.StockPriceHistory;
import com.investme.backend.entity.User;
import com.investme.backend.entity.UserActionLog;
import com.investme.backend.repository.StockPriceHistoryRepository;
import com.investme.backend.repository.StockRepository;
import com.investme.backend.repository.UserActionLogRepository;
import com.investme.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private final StockRepository stockRepository;
    private final StockPriceHistoryRepository stockPriceHistoryRepository;
    private final UserRepository userRepository;
    private final UserActionLogRepository userActionLogRepository;

    @Transactional
    public List<SurgingStockResponse> getAiRecommendations(
            String loginUserId
    ) {

        User user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        ));

        List<Stock> stocks =
                stockRepository.findAll();

        LocalDateTime threeMinutesAgo =
                LocalDateTime.now().minusMinutes(3);

        List<AiCandidate> candidates =
                new ArrayList<>();

        // 1. 후보 판정 + 3분 전 가격 함께 저장
        for (Stock stock : stocks) {

            Optional<StockPriceHistory> historyOptional =
                    stockPriceHistoryRepository
                            .findTopByStockIdAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
                                    stock.getStockId(),
                                    threeMinutesAgo
                            );

            if (historyOptional.isEmpty()) {
                continue;
            }

            int previousPrice =
                    historyOptional.get().getPrice();

            int currentPrice =
                    stock.getCurrentPrice();

            if (previousPrice <= 0) {
                continue;
            }

            double changeRate =
                    (double) (currentPrice - previousPrice)
                            / previousPrice
                            * 100;

            if (changeRate >= 5.0) {
                candidates.add(
                        new AiCandidate(
                                stock,
                                previousPrice
                        )
                );
            }
        }

        // 2. 후보 랜덤 선택
        Collections.shuffle(candidates);

        List<AiCandidate> selectedCandidates =
                candidates.stream()
                        .limit(3)
                        .toList();

        List<SurgingStockResponse> response =
                new ArrayList<>();

        // 3. History 재조회 없이 저장한 previousPrice 사용
        for (AiCandidate candidate : selectedCandidates) {

            Stock stock =
                    candidate.stock;

            int previousPrice =
                    candidate.previousPrice;

            int currentPrice =
                    stock.getCurrentPrice();

            int priceChange =
                    currentPrice - previousPrice;

            double changeRate =
                    (double) priceChange
                            / previousPrice
                            * 100;

            UserActionLog exposure =
                    new UserActionLog(
                            user.getId(),
                            stock.getStockId(),
                            "EXPOSURE",
                            "AI_RECOMMENDATION",
                            null
                    );

            UserActionLog savedExposure =
                    userActionLogRepository.save(exposure);

            response.add(
                    new SurgingStockResponse(
                            savedExposure.getActionId(),
                            stock.getStockId(),
                            stock.getName(),
                            currentPrice,
                            priceChange,
                            round(changeRate)
                    )
            );
        }

        return response;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static class AiCandidate {

        private final Stock stock;
        private final int previousPrice;

        private AiCandidate(
                Stock stock,
                int previousPrice
        ) {
            this.stock = stock;
            this.previousPrice = previousPrice;
        }
    }
}