package com.investme.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {
    private String stockId;
    private String side;
    private String orderType;
    private int quantity;
    private Integer price;
    private Long triggeredByEventId;
}
