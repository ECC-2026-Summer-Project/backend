package com.investme.backend.controller;

import com.investme.backend.dto.ApiResponse;
import com.investme.backend.dto.PortfolioResponse;
import com.investme.backend.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/holdings")
    public ApiResponse<PortfolioResponse> getHoldings(
            Authentication authentication
    ) {

        String userId = authentication.getName();

        PortfolioResponse response =
                portfolioService.getHoldings(userId);

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }
}