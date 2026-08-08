package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NewsDetailResponse {

    private Long newsId;
    private String title;
    private String content;
    private LocalDateTime publishedAt;
}