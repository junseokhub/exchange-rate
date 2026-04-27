package com.dev.forex.batch;

import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import com.dev.forex.external.ExchangeRateCollector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@StepScope
public class ExchangeRateReader implements ItemReader<ExchangeRateHistory> {

    private final ExchangeRateCollector exchangeRateCollector;
    private List<ExchangeRateHistory> histories;
    private int index = 0;

    @Override
    public ExchangeRateHistory read() {
        if (histories == null) {
            histories = exchangeRateCollector.collect();
            log.info("환율 데이터 수집 완료: {}건", histories.size());
        }
        if (index < histories.size()) {
            return histories.get(index++);
        }
        histories = null;
        index = 0;
        return null;
    }
}