package com.investme.backend.repository;

import com.investme.backend.entity.PsychologyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PsychologyReportRepository
        extends JpaRepository<PsychologyReport, Long> {

    Optional<PsychologyReport> findByReportIdAndUserId(
            Long reportId,
            Long userId
    );

    Optional<PsychologyReport> findTopByUserIdOrderByCreatedAtDesc(
            Long userId
    );
}