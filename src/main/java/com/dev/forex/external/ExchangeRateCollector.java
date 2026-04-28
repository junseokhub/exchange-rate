package com.dev.forex.external;

import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;

import java.util.List;

public interface ExchangeRateCollector {
    List<ExchangeRateHistory> collect();
}
