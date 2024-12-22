package com.news.backend.controller;

import com.news.backend.global.ApiResponse;
import com.news.backend.global.ErrorCode;
import com.news.backend.service.JwtTokenService;
import com.news.backend.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final JwtTokenService jwtTokenService;
    private final RedisService redisService;

    @PostMapping("/api/token/refresh")
    public ResponseEntity<ApiResponse<Map<String, String>>> refreshAccessToken(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String refreshToken = request.get("refreshToken");

        // 리프레시 토큰 검증 실패 시 에러 반환
        if (!redisService.validateRefreshToken(email, refreshToken)) {
            return ResponseEntity.status(401).body(
                    ApiResponse.<Map<String, String>>error(ErrorCode.INVALID_TOKEN)
            );
        }

        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtTokenService.generateAccessToken(email);

        return ResponseEntity.ok(
                ApiResponse.success(Map.of(
                        "accessToken", newAccessToken
                ))
        );
    }
}
