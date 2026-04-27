package com.dev.forex.domain.exchangerate.service;

import com.dev.forex.domain.currency.CurrencyType;
import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;

import java.util.List;

public interface ExchangeRateService {

    List<ExchangeRateHistory> getLatestAll();

    ExchangeRateHistory getLatestByCurrency(CurrencyType currency);

    void saveAll(List<ExchangeRateHistory> exchangeRateHistories);
}
