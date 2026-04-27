package com.dev.forex.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateScheduler {

    private final JobLauncher jobLauncher;
    private final Job exchangeRateJob;

    @Scheduled(fixedDelay = 60000)
    public void runExchangeRateJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLocalDateTime("requestTime", LocalDateTime.now())
                    .toJobParameters();

            jobLauncher.run(exchangeRateJob, jobParameters);
            log.info("환율 수집 Job 실행 완료");
        } catch (Exception e) {
            log.error("환율 수집 Job 실행 실패: {}", e.getMessage());
        }
    }
}