package com.investme.backend.controller;

import com.investme.backend.dto.ApiResponse;
import com.investme.backend.dto.ReportCreateResponse;
import com.investme.backend.dto.ReportResponse;
import com.investme.backend.entity.User;
import com.investme.backend.repository.UserRepository;
import com.investme.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepository;


    // =========================
    // 레포트 생성
    // POST /api/reports
    // =========================

    @PostMapping
    public ResponseEntity<ApiResponse<ReportCreateResponse>> createReport(
            Authentication authentication
    ) {

        String loginUserId =
                authentication.getName();

        User user =
                userRepository.findByUserId(loginUserId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "사용자를 찾을 수 없습니다."
                                )
                        );

        ReportCreateResponse response =
                reportService.createReportResponse(
                        user.getId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                response,
                                null
                        )
                );
    }


    // =========================
    // 레포트 조회
    // GET /api/reports/{reportId}
    // =========================

    @GetMapping("/{reportId}")
    public ApiResponse<ReportResponse> getReport(
            @PathVariable Long reportId,
            Authentication authentication
    ) {

        String loginUserId =
                authentication.getName();

        User user =
                userRepository.findByUserId(loginUserId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "사용자를 찾을 수 없습니다."
                                )
                        );

        ReportResponse response =
                reportService.getReport(
                        reportId,
                        user.getId()
                );

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }
}
