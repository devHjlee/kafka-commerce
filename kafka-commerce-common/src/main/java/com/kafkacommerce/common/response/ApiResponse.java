package com.kafkacommerce.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private final boolean success;
    private final String code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, "SUCCESS", message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, "SUCCESS", message, null);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, "ERROR", message, null);
    }
} 