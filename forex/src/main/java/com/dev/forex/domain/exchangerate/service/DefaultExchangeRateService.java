package com.dev.forex.domain.exchangerate.service;

import com.dev.forex.common.exception.BusinessException;
import com.dev.forex.common.exception.ErrorCode;
import com.dev.forex.domain.currency.CurrencyType;
import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import com.dev.forex.domain.exchangerate.repository.ExchangeRateHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultExchangeRateService implements ExchangeRateService {

    private final ExchangeRateHistoryRepository exchangeRateHistoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ExchangeRateHistory> getLatestAll() {
        return Arrays.stream(CurrencyType.values())
                .filter(currency -> currency != CurrencyType.KRW)
                .map(exchangeRateHistoryRepository::findTopByCurrencyOrderByCollectedAtDesc)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExchangeRateHistory getLatestByCurrency(CurrencyType currencyType) {
        return exchangeRateHistoryRepository
                .findTopByCurrencyOrderByCollectedAtDesc(currencyType)
                .orElseThrow(() -> new BusinessException(ErrorCode.EXCHANGE_RATE_NOT_FOUND));
    }

    @Override
    @Transactional
    public void saveAll(List<ExchangeRateHistory> exchangeRateHistories) {
        exchangeRateHistoryRepository.saveAll(exchangeRateHistories);
    }
}
