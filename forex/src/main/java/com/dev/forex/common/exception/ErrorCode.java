package com.dev.forex.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    CURRENCY_NOT_SUPPORTED("BAD_REQUEST", "지원하지 않는 통화", HttpStatus.BAD_REQUEST),
    EXTERNAL_API_ERROR("CONNECT", "외부 연동 오류 발생", HttpStatus.BAD_GATEWAY)
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
