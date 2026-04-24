package com.dev.forex.common.response;

import com.dev.forex.common.exception.ErrorCode;

public record ApiResponse<T> (
        String code,
        String message,
        T returnObject
){

    public static <T> ApiResponse<T> success(T returnObject) {
        return new ApiResponse<>("OK", "SUCCESS", returnObject);
    }

    public static ApiResponse<Void> fail(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static ApiResponse<Void> fail(String message) {
        return new ApiResponse<>("ERROR", message, null);
    }
}
