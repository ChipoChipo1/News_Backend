package com.news.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 리프레시 토큰 저장
    public void saveRefreshToken(String email, String refreshToken, long expiration) {
        redisTemplate.opsForValue().set(email, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    // 리프레시 토큰 검증
    public boolean validateRefreshToken(String email, String refreshToken) {
        String storedToken = (String) redisTemplate.opsForValue().get(email);
        return storedToken != null && storedToken.equals(refreshToken);
    }

    // 리프레시 토큰 삭제 (로그아웃 시)
    public void deleteRefreshToken(String email) {
        redisTemplate.delete(email);
    }
}
