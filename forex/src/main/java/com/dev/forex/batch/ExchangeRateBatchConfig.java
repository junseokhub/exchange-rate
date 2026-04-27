package com.dev.forex.batch;

import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import com.dev.forex.domain.exchangerate.repository.ExchangeRateHistoryRepository;
import com.dev.forex.external.ExchangeRateCollector;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ExchangeRateBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ExchangeRateCollector exchangeRateCollector;
    private final ExchangeRateHistoryRepository exchangeRateHistoryRepository;

    @Bean
    public Job exchangeRateJob() {
        return new JobBuilder("exchangeRateJob", jobRepository)
                .start(exchangeRateStep())
                .build();
    }

    @Bean
    public Step exchangeRateStep() {
        return new StepBuilder("exchangeRateStep", jobRepository)
                .<ExchangeRateHistory, ExchangeRateHistory>chunk(4, transactionManager)
                .reader(exchangeRateReader())
                .processor(exchangeRateProcessor())
                .writer(exchangeRateWriter())
                .build();
    }

    @Bean
    @StepScope
    public ExchangeRateReader exchangeRateReader() {
        return new ExchangeRateReader(exchangeRateCollector);
    }

    @Bean
    public ExchangeRateProcessor exchangeRateProcessor() {
        return new ExchangeRateProcessor();
    }

    @Bean
    public ExchangeRateWriter exchangeRateWriter() {
        return new ExchangeRateWriter(exchangeRateHistoryRepository);
    }
}