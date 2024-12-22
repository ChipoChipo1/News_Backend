package com.news.backend.controller;

import com.news.backend.global.ApiResponse;
import com.news.backend.service.KeywordService;
import com.news.backend.service.NewsService;
import com.news.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final KeywordService keywordService;
    private final NewsService newsService;
    private final UserService userService;

    // 관심 키워드 설정
    @PostMapping("/api/user/keywords")
    public ResponseEntity<ApiResponse<Void>> setUserKeywords(@RequestBody List<String> keywords, @RequestParam String email) {
        keywordService.setUserKeywords(keywords, email);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 사용자의 관심 뉴스 키워드 기반 추천
    @GetMapping("/api/news/recommend")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> recommendNews(@RequestParam String email) {
        List<Map<String, String>> recommendedNews = newsService.getRecommendedNews(email);  // 수정된 부분
        return ResponseEntity.ok(ApiResponse.success(recommendedNews));
    }

    // 사용자 프로필 정보 조회
    @GetMapping("/api/user/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserProfile(@RequestParam String email) {
        Map<String, Object> profile = userService.getUserProfile(email);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    // 사용자 프로필 수정
    @PutMapping("/api/user/profile")
    public ResponseEntity<ApiResponse<Void>> updateUserProfile(@RequestBody Map<String, String> updatedProfile) {
        userService.updateUserProfile(updatedProfile);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 사용자의 키워드 삭제
    @DeleteMapping("/api/user/keywords")
    public ResponseEntity<ApiResponse<Void>> deleteUserKeyword(@RequestParam String email, @RequestParam String keyword) {
        keywordService.deleteUserKeyword(email, keyword);
        return ResponseEntity.ok(ApiResponse.success(null));
    }



}
