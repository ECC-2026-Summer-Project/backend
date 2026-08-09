package com.investme.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsViewRequest {

    @NotNull
    @Min(0)
    private Integer durationSeconds;
}