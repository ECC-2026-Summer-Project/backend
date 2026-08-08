package com.investme.backend.controller;

import com.investme.backend.dto.ApiResponse;
import com.investme.backend.dto.SurgingStockResponse;
import com.investme.backend.service.AiRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    @GetMapping("/ai")
    public ApiResponse<List<SurgingStockResponse>> getAiRecommendations(
            Authentication authentication
    ) {

        String userId = authentication.getName();

        List<SurgingStockResponse> response =
                aiRecommendationService.getAiRecommendations(userId);

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }
}
