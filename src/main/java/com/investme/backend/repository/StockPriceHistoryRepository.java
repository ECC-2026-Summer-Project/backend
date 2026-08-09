package com.investme.backend.repository;

import com.investme.backend.entity.StockPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockPriceHistoryRepository
        extends JpaRepository<StockPriceHistory, Long> {

    Optional<StockPriceHistory>
    findTopByCompanyIdOrderByCreatedAtDesc(String companyId);
}