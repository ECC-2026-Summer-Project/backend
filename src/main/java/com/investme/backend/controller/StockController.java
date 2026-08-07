package com.investme.backend.controller;

import com.investme.backend.dto.ApiResponse;
import com.investme.backend.dto.stock.StockChartResponse;
import com.investme.backend.dto.stock.StockSummaryResponse;
import com.investme.backend.service.StockService;
import org.springframework.web.bind.annotation.*;
import com.investme.backend.dto.stock.StockOrderBookResponse;

import java.util.List;

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

    @GetMapping("/{stockId}/chart")
    public ApiResponse<List<StockChartResponse>> getChart(
            @PathVariable String stockId,
            @RequestParam(defaultValue = "1d") String interval,
            @RequestParam(defaultValue = "1d") String range
    ) {

        List<StockChartResponse> response = stockService.getChart(stockId, interval, range);

        return new ApiResponse<>(true, response, null);
    }

    @GetMapping("/{stockId}/orderbook")
    public ApiResponse<StockOrderBookResponse> getOrderBook(
            @PathVariable String stockId
    ) {

        StockOrderBookResponse response = stockService.getOrderBook(stockId);

        return new ApiResponse<>(true, response, null);
    }
}