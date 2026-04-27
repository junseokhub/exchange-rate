package com.dev.forex.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderRequest(
        @NotNull(message = "외화 금액은 필수")
        @Positive(message = "외화 금액은 양수")
        BigDecimal forexAmount,

        @NotBlank(message = "출발 통화는 필수")
        String fromCurrency,

        @NotBlank(message = "도착 통화는 필수")
        String toCurrency
) {
}