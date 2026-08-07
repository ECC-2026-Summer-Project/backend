package com.investme.backend.repository;

import com.investme.backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, String> {

    Optional<Company> findByCompanyId(String companyId);

    boolean existsByCompanyId(String companyId);
}
