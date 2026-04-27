package com.dev.forex.domain.currency;

import com.dev.forex.common.exception.BusinessException;
import com.dev.forex.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CurrencyType {
    KRW(1),
    USD(1),
    JPY(100),
    CNY(1),
    EUR(1);

    private final int baseUnit;

    public static CurrencyType from(String code) {
        return Arrays.stream(CurrencyType.values())
                .filter(currency -> currency.name().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.CURRENCY_NOT_SUPPORTED));
    }
}
