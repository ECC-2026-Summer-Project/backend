package com.investme.backend.controller;

import com.investme.backend.domain.Stock;
import com.investme.backend.domain.TradeHistory;
import com.investme.backend.dto.OrderRequest;
import com.investme.backend.dto.OrderResponse;
import com.investme.backend.repository.StockRepository;
import com.investme.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final StockRepository stockRepository;

    @PostMapping
    public OrderResponse createOrder(@AuthenticationPrincipal String loginUserId, @RequestBody OrderRequest request) {
        TradeHistory trade = orderService.executeOrder(loginUserId, request);
        Stock stock = stockRepository.findById(trade.getStockId()).orElseThrow();

        OrderResponse.Data data = new OrderResponse.Data(
                String.valueOf(trade.getTradeId()), trade.getStockId(), stock.getName(),
                trade.getSide(), trade.getOrderType(), trade.getQuantity(), trade.getPrice(),
                trade.getTotalAmount(), trade.getStatus(), trade.getCreatedAt());

        return new OrderResponse(true, data);
    }
}
