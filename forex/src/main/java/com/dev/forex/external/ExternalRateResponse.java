package com.dev.forex.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

public record ExternalRateResponse(
        String result,
        @JsonProperty("base_code") String baseCode,
        Map<String, BigDecimal> rates
) {
    public boolean isSuccess() {
        return "success".equals(result);
    }

    public BigDecimal getRate(String currency) {
        return rates.get(currency);
    }
}