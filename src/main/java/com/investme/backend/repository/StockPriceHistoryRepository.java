package com.investme.backend.repository;

import com.investme.backend.entity.StockPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface StockPriceHistoryRepository
        extends JpaRepository<StockPriceHistory, Long> {

    // 해당 종목의 가장 최근 가격 기록
    Optional<StockPriceHistory>
    findTopByStockIdOrderByCreatedAtDesc(String stockId);

    // 특정 시각 이전(포함)의 가격 중 가장 최근 기록
    Optional<StockPriceHistory>
    findTopByStockIdAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
            String stockId,
            LocalDateTime createdAt
    );
}