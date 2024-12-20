package com.news.backend.global;

public class LoginFailedException extends BusinessException {
    public LoginFailedException() {
        super(ErrorCode.LOGIN_FAILED);
    }
}