package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReportCreateResponse {

    private Long reportId;
    private LocalDateTime createdAt;
}