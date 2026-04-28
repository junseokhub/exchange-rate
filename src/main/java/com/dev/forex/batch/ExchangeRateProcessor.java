package com.dev.forex.batch;

import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

@Slf4j
public class ExchangeRateProcessor implements ItemProcessor<ExchangeRateHistory, ExchangeRateHistory> {

    @Override
    public ExchangeRateHistory process(ExchangeRateHistory item) {
        if (item.getTradeStanRate().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("비정상 환율 필터링: {} - {}", item.getCurrency(), item.getTradeStanRate());
            return null;
        }
        return item;
    }
}