package com.news.backend.global;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .message("Success")
                .data(data)
                .build();
    }

    // 일반 에러 응답
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();
    }

    // 커스텀 메시지 에러 응답
    public static <T> ApiResponse<T> error(ErrorCode errorCode, String customMessage) {
        return ApiResponse.<T>builder()
                .status(errorCode.getStatus())
                .message(customMessage)
                .build();
    }
}