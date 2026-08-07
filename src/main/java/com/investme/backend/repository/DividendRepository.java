package com.investme.backend.repository;

import com.investme.backend.entity.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DividendRepository extends JpaRepository<Dividend, Long> {

    // 특정 종목의 배당 이력을 연도 내림차순(최신순)으로 조회
    List<Dividend> findByCompany_CompanyIdOrderByYearDesc(String companyId);
}