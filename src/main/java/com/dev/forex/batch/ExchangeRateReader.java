package com.dev.forex.batch;

import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import com.dev.forex.external.ExchangeRateCollector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@StepScope
public class ExchangeRateReader implements ItemReader<ExchangeRateHistory> {

    private final ExchangeRateCollector exchangeRateCollector;
    private Iterator<ExchangeRateHistory> iterator;

    @Override
    public ExchangeRateHistory read() {
        if (iterator == null) {
            List<ExchangeRateHistory> histories = exchangeRateCollector.collect();
            log.info("환율 데이터 수집 완료: {}건", histories.size());
            iterator = histories.iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }
}