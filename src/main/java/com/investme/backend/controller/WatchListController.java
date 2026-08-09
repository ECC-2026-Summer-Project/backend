package com.investme.backend.controller;

import com.investme.backend.domain.Stock;
import com.investme.backend.domain.WatchList;
import com.investme.backend.dto.WatchListItemDto;
import com.investme.backend.dto.WatchListResponse;
import com.investme.backend.dto.WatchListActionResponse;
import com.investme.backend.entity.User;
import com.investme.backend.exception.ApiException;
import com.investme.backend.repository.StockRepository;
import com.investme.backend.repository.UserRepository;
import com.investme.backend.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchListController {

    private final WatchListService watchListService;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    @GetMapping
    public WatchListResponse getWatchList(@AuthenticationPrincipal String loginUserId) {
        User user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "존재하지 않는 유저입니다."));
        List<WatchListItemDto> items = watchListService.getWatchList(user.getId());
        return new WatchListResponse(true, items, items.size());
    }

    @PostMapping("/{stockId}")
    public WatchListActionResponse addWatchList(@AuthenticationPrincipal String loginUserId, @PathVariable String stockId) {
        User user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "존재하지 않는 유저입니다."));
        WatchList saved = watchListService.addToWatchList(user.getId(), stockId);
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "STOCK_NOT_FOUND", "존재하지 않는 종목입니다."));
        return new WatchListActionResponse(true, "관심 종목에 추가되었습니다.",
                Map.of("stockId", stock.getStockId(), "name", stock.getName(), "addedAt", saved.getCreatedAt()));
    }

    @DeleteMapping("/{stockId}")
    public WatchListActionResponse removeWatchList(@AuthenticationPrincipal String loginUserId, @PathVariable String stockId) {
        User user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "존재하지 않는 유저입니다."));
        watchListService.removeFromWatchList(user.getId(), stockId);
        return new WatchListActionResponse(true, "관심 종목에서 삭제되었습니다.", Map.of("stockId", stockId));
    }
}
