package com.news.backend.global;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public <T> ResponseEntity<ApiResponse<T>> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(ApiResponse.error(ex.getErrorCode()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}