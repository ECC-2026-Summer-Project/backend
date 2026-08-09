package com.investme.backend.repository;

import com.investme.backend.domain.TradeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeHistoryRepository
        extends JpaRepository<TradeHistory, Long> {

    Page<TradeHistory> findByStockIdOrderByCreatedAtDesc(
            String stockId,
            Pageable pageable
    );
}