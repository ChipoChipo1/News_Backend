package com.news.backend.global;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "잘못된 입력입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "인증이 필요합니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러가 발생했습니다."),

    // 로그인 관련 에러
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED.value(), "로그인에 실패했습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다."),
    OAUTH_PROVIDER_ERROR(HttpStatus.BAD_REQUEST.value(), "지원하지 않는 OAuth 제공자입니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "이미 존재하는 사용자입니다."),
    USER_NOT_REGISTERED(HttpStatus.BAD_REQUEST.value(), "등록되지 않은 사용자입니다."),

    ;//

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}