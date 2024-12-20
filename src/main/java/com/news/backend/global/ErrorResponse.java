package com.news.backend.global;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private int status;
    private String message;

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();
    }
}