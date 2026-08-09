package com.investme.backend.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder

public class StockListResponse {
    private boolean success;
    private List<StockListItemDto> data;
    private long total;
    private int page;
    private int pageSize;
}
