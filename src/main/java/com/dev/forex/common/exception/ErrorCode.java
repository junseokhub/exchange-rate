package com.dev.forex.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    CURRENCY_NOT_SUPPORTED("BAD_REQUEST", "지원하지 않는 통화", HttpStatus.BAD_REQUEST),
    EXTERNAL_API_ERROR("SERVICE_UNAVAILABLE", "외부 연동 오류 발생", HttpStatus.SERVICE_UNAVAILABLE),
    EXCHANGE_RATE_NOT_FOUND("NOT_FOUND", "환율 정보를 찾을 수 없음", HttpStatus.NOT_FOUND),
    SAME_CURRENCY("BAD_REQUEST", "동일 통화 주문 불가", HttpStatus.BAD_REQUEST),
    REQUIRED_KRW_CURRENCY("BAD_REQUEST", "KRW을 포함해야 함", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_REQUEST("BAD_REQUEST", "잘못된 주문 요청", HttpStatus.BAD_REQUEST),
    ;


    private final String code;
    private final String message;
    private final HttpStatus status;
    }
