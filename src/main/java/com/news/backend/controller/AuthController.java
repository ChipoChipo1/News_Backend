package com.news.backend.controller;

import com.news.backend.domain.User;
import com.news.backend.dto.OAuthUserInfo;
import com.news.backend.global.ApiResponse;
import com.news.backend.global.ErrorCode;
import com.news.backend.service.JwtTokenService;
import com.news.backend.service.OAuthService;
import com.news.backend.service.RedisService;
import com.news.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final OAuthService oAuthService;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final RedisService redisService;

    // 로그인 성공 핸들러
    @GetMapping("/api/login/success")
    public ResponseEntity<ApiResponse<Map<String, Object>>> loginSuccess(OAuth2AuthenticationToken token) {
        // 사용자 정보 가져오기
        OAuthUserInfo userInfo = oAuthService.getUserInfo(token);

        // 액세스 토큰 및 리프레시 토큰 생성
        String accessToken = jwtTokenService.generateAccessToken(userInfo.getEmail());
        String refreshToken = jwtTokenService.generateRefreshToken(userInfo.getEmail());


        // 사용자 정보 저장 및 리프레시 토큰 업데이트
        User user = userService.saveOrUpdateUser(userInfo, refreshToken);

        // 응답 반환
        return ResponseEntity.ok(
                ApiResponse.success(
                        Map.of(
                                "accessToken", accessToken,
                                "refreshToken", refreshToken
                        )
                )
        );
    }

    // 로그인 실패 핸들러
    @GetMapping("/api/login/failure")
    public ResponseEntity<ApiResponse<Void>> loginFailure() {
        return ResponseEntity.status(401).body(
                ApiResponse.error(ErrorCode.UNAUTHORIZED)
        );
    }

    @GetMapping("/api/logout")
    public ResponseEntity<ApiResponse<Void>> logout(OAuth2AuthenticationToken token) {
        OAuthUserInfo userInfo = oAuthService.getUserInfo(token);

        // Redis에서 리프레시 토큰 삭제
        redisService.deleteRefreshToken(userInfo.getEmail());

        return ResponseEntity.ok(
                ApiResponse.success(null)
        );
    }

}
