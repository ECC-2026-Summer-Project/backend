package com.investme.backend.repository;

import com.investme.backend.entity.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Long> {

    // 특정 종목의 최근 체결 내역을 최신순으로 limit개 조회
    List<TradeHistory> findByCompany_CompanyIdOrderByTradedAtDesc(String companyId, Pageable pageable);
}
