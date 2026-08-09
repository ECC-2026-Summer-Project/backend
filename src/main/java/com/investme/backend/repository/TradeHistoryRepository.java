package com.investme.backend.repository;

import com.investme.backend.domain.TradeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeHistoryRepository
        extends JpaRepository<TradeHistory, Long> {

    // 기업별 최근 체결 내역 조회
    Page<TradeHistory> findByStockIdOrderByCreatedAtDesc(
            String stockId,
            Pageable pageable
    );

    // 사용자별 거래 내역 조회
    List<TradeHistory> findAllByUserId(Long userId);

    // 사용자별 매수/매도 횟수 조회
    long countByUserIdAndSide(Long userId, String side);

    // 이벤트에 의해 발생한 거래 내역 조회
    List<TradeHistory> findAllByUserIdAndTriggeredByEventIdIsNotNull(
            Long userId
    );
}