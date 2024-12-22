package com.news.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

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




    // 조회수 증가
    public void incrementViewCount(String newsId) {
        String key = "news:views";
        redisTemplate.opsForZSet().incrementScore(key, newsId, 1); // 조회수 1 증가
    }

    // 주간 랭킹 조회
    public Set<String> getWeeklyRanking(int topN) {
        String key = "news:views";
        return redisTemplate.opsForZSet().reverseRange(key, 0, topN - 1); // 상위 N개 뉴스 ID 반환
    }

    // 특정 뉴스의 조회수 가져오기
    public Double getViewCount(String newsId) {
        String key = "news:views";
        return redisTemplate.opsForZSet().score(key, newsId);
    }
}
