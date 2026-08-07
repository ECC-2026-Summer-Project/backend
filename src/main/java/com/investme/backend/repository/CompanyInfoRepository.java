package com.investme.backend.repository;

import com.investme.backend.entity.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, String> {

    Optional<CompanyInfo> findByCompanyId(String companyId);
}
