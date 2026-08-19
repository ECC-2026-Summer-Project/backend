package com.investme.backend.service;

import com.investme.backend.domain.Stock;
import com.investme.backend.dto.HoldingResponse;
import com.investme.backend.dto.PortfolioResponse;
import com.investme.backend.entity.User;
import com.investme.backend.entity.UserStock;
import com.investme.backend.repository.StockRepository;
import com.investme.backend.repository.UserRepository;
import com.investme.backend.repository.UserStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioService {

    private final UserRepository userRepository;
    private final UserStockRepository userStockRepository;
    private final StockRepository stockRepository;

    public PortfolioResponse getHoldings(String loginUserId) {

        // 1. JWT에서 받은 로그인 아이디로 사용자 조회
        User user = userRepository.findByUserId(loginUserId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        ));

        // 2. DB PK를 이용해 사용자의 보유 종목 조회
        List<UserStock> userStocks =
                userStockRepository.findAllByUserId(user.getId());

        List<HoldingResponse> holdings =
                new ArrayList<>();

        long totalPurchaseAmount = 0L;
        long totalEvaluationAmount = 0L;

        // 3. 종목별 정보 계산
        for (UserStock userStock : userStocks) {

            Stock stock = stockRepository
                    .findById(userStock.getStockId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "종목을 찾을 수 없습니다."
                            ));

            int quantity =
                    userStock.getQuantity();

            int averagePurchasePrice =
                    userStock.getAveragePrice();

            int currentPrice =
                    stock.getCurrentPrice();

            long purchaseAmount =
                    userStock.getTotalAmount();

            long evaluationAmount =
                    (long) quantity * currentPrice;

            long profitLoss =
                    evaluationAmount - purchaseAmount;

            double returnRate =
                    purchaseAmount == 0
                            ? 0.0
                            : (double) profitLoss
                            / purchaseAmount
                            * 100;

            int priceChange =
                    stock.getChangeAmount();

            double changeRate =
                    stock.getChangeRate();

            holdings.add(
                    new HoldingResponse(
                            stock.getStockId(),
                            stock.getName(),
                            quantity,
                            averagePurchasePrice,
                            currentPrice,
                            priceChange,
                            round(changeRate),
                            purchaseAmount,
                            evaluationAmount,
                            profitLoss,
                            round(returnRate)
                    )
            );

            totalPurchaseAmount +=
                    purchaseAmount;

            totalEvaluationAmount +=
                    evaluationAmount;
        }

        // 4. 전체 손익 계산
        long totalProfitLoss =
                totalEvaluationAmount
                        - totalPurchaseAmount;

        double totalReturnRate =
                totalPurchaseAmount == 0
                        ? 0.0
                        : (double) totalProfitLoss
                        / totalPurchaseAmount
                        * 100;

        // 5. 응답
        return new PortfolioResponse(
                totalPurchaseAmount,
                totalEvaluationAmount,
                totalProfitLoss,
                round(totalReturnRate),
                holdings
        );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}