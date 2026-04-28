package com.dev.forex.domain.exchangerate.repository;

import com.dev.forex.domain.currency.CurrencyType;
import com.dev.forex.domain.exchangerate.entity.ExchangeRateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateHistoryRepository extends JpaRepository<ExchangeRateHistory, Long> {

    //  특정 통화 최신 환율 상세 조회
    Optional<ExchangeRateHistory> findTopByCurrencyOrderByCreatedAtDesc(CurrencyType currency);
}
