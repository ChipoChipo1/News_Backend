package com.news.backend.global;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    public final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}