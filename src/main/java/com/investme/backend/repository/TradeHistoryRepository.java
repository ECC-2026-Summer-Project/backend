package com.investme.backend.repository;

import com.investme.backend.domain.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeHistoryRepository
        extends JpaRepository<TradeHistory, Long> {

    List<TradeHistory> findAllByUserId(Long userId);

    long countByUserIdAndSide(Long userId, String side);

    List<TradeHistory> findAllByUserIdAndTriggeredByEventIdIsNotNull(
            Long userId
    );
}