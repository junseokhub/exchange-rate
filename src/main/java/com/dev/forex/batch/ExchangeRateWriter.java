package com.dev.forex.batch;

import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import com.dev.forex.domain.exchangerate.repository.ExchangeRateHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
@RequiredArgsConstructor
public class ExchangeRateWriter implements ItemWriter<ExchangeRateHistory> {

    private final ExchangeRateHistoryRepository exchangeRateHistoryRepository;

    @Override
    public void write(Chunk<? extends ExchangeRateHistory> chunk) {
        exchangeRateHistoryRepository.saveAll(chunk.getItems());
        log.info("환율 데이터 저장 완료: {}건", chunk.getItems().size());
    }
}