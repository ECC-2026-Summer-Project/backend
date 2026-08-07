package com.investme.backend.repository;

import com.investme.backend.entity.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DividendRepository extends JpaRepository<Dividend, Long> {

    List<Dividend> findByCompany_CompanyIdOrderByYearDesc(String companyId);
}