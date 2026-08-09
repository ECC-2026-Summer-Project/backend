package com.investme.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WatchListActionResponse {
    private boolean success;
    private String message;
    private Object data;
}
