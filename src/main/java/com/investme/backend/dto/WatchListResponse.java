package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class WatchListResponse {
    private boolean success;
    private List<WatchListItemDto> data;
    private long total;
}
