package com.investme.backend.service;

import com.investme.backend.domain.Stock;
import com.investme.backend.dto.AccountSummaryResponse;
import com.investme.backend.entity.User;
import com.investme.backend.entity.UserStock;
import com.investme.backend.repository.StockRepository;
import com.investme.backend.repository.UserRepository;
import com.investme.backend.repository.UserStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final UserRepository userRepository;
    private final UserStockRepository userStockRepository;
    private final StockRepository stockRepository;

    public AccountSummaryResponse getAccountSummary(String loginUserId) {

        User user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        ));

        Long userPk = user.getId();

        List<UserStock> holdings =
                userStockRepository.findAllByUserId(userPk);

        long cashBalance = user.getCashBalance();

        long totalPurchaseAmount = holdings.stream()
                .mapToLong(UserStock::getTotalAmount)
                .sum();

        long stockEvaluationAmount = holdings.stream()
                .mapToLong(holding -> {

                    Stock stock = stockRepository
                            .findById(holding.getStockId())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "종목을 찾을 수 없습니다."
                                    ));

                    return (long) holding.getQuantity()
                            * stock.getCurrentPrice();
                })
                .sum();

        long totalAssetAmount =
                cashBalance + stockEvaluationAmount;

        long totalProfitLoss =
                stockEvaluationAmount - totalPurchaseAmount;

        double totalReturnRate =
                totalPurchaseAmount == 0
                        ? 0.0
                        : (double) totalProfitLoss
                        / totalPurchaseAmount
                        * 100;

        return new AccountSummaryResponse(
                totalAssetAmount,
                totalProfitLoss,
                round(totalReturnRate)
        );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}