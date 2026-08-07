package com.investme.backend.controller;

import com.investme.backend.dto.ApiResponse;
import com.investme.backend.dto.NewsDetailResponse;
import com.investme.backend.dto.NewsListResponse;
import com.investme.backend.dto.NewsViewRequest;
import com.investme.backend.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ApiResponse<List<NewsListResponse>> getNewsList(
            @RequestParam(required = false)
            List<Long> excludeNewsIds
    ) {

        List<NewsListResponse> response =
                newsService.getNewsList(excludeNewsIds);

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }

    @GetMapping("/{newsId}")
    public ApiResponse<NewsDetailResponse> getNewsDetail(
            @PathVariable Long newsId,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        NewsDetailResponse response =
                newsService.getNewsDetail(
                        newsId,
                        userId
                );

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }

    @PostMapping("/{newsId}/views")
    public ResponseEntity<ApiResponse<Void>> completeNewsView(
            @PathVariable Long newsId,
            @Valid @RequestBody NewsViewRequest request,
            Authentication authentication
    ) {

        String userId = authentication.getName();

        newsService.completeNewsView(
                newsId,
                userId,
                request.getDurationSeconds()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        null,
                        null
                ));
    }
}
