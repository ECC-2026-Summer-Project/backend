package com.investme.backend.controller;

import com.investme.backend.dto.ApiResponse;
import com.investme.backend.dto.SurgingStockResponse;
import com.investme.backend.service.SurgingStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
public class StockController {

    private final SurgingStockService surgingStockService;

    @GetMapping("/surging")
    public ApiResponse<List<SurgingStockResponse>> getSurgingStocks(
            Authentication authentication
    ) {

        String userId = authentication.getName();

        List<SurgingStockResponse> response =
                surgingStockService.getSurgingStocks(userId);

        return new ApiResponse<>(
                true,
                response,
                null
        );
    }
}
