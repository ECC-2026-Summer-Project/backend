package com.investme.backend.controller;

import com.investme.backend.dto.ApiResponse;
import com.investme.backend.dto.stock.StockSummaryResponse;
import com.investme.backend.service.StockService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{stockId}/summary")
    public ApiResponse<StockSummaryResponse> getSummary(
            @PathVariable String stockId
    ) {

        StockSummaryResponse response = stockService.getSummary(stockId);

        return new ApiResponse<>(true, response, null);
    }
}