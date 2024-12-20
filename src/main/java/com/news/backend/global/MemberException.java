package com.news.backend.global;

public class MemberException extends BusinessException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}