package com.dev.forex.batch;

import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class ExchangeRateProcessor implements ItemProcessor<ExchangeRateHistory, ExchangeRateHistory> {

    @Override
    public ExchangeRateHistory process(ExchangeRateHistory item) {
        log.debug("환율 데이터 처리: {} - {}", item.getCurrency(), item.getTradeStanRate());
        return item;
    }
}