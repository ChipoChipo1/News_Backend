package com.news.backend.controller;

import com.news.backend.domain.News;
import com.news.backend.global.ApiResponse;
import com.news.backend.repository.NewsRepository;
import com.news.backend.service.NewsService;
import com.news.backend.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;
    private final StringRedisTemplate redisTemplate;
    private final RedisService redisService;
    private final NewsRepository newsRepository;

    // 뉴스 조회수 증가
    @PostMapping("/view/{newsId}")
    public ResponseEntity<Void> incrementViewCount(@PathVariable String newsId) {
        newsService.incrementViewCount(newsId);
        return ResponseEntity.ok().build();
    }

    // 주간 뉴스 랭킹 조회
    @GetMapping("/weekly-ranking")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getWeeklyRanking(@RequestParam(defaultValue = "10") int topN) {
        String key = "news:views";
        Set<String> rankingSet = redisTemplate.opsForZSet().reverseRange(key, 0, topN - 1);

        if (rankingSet == null || rankingSet.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(List.of())); // 빈 리스트 반환
        }

        List<Map<String, Object>> rankings = rankingSet.stream()
                .map(newsId -> {
                    News news = newsService.getNewsById(newsId); // NewsService 호출

                    Double viewCount = newsService.getViewCount(newsId); // 조회수 가져오기

                    Map<String, Object> map = new HashMap<>();
                    map.put("id", news.getId());
                    map.put("title", news.getTitle());
                    map.put("viewCount", viewCount);

                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(rankings));
    }
    // 뉴스 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> searchNews(@RequestParam String query) {
        List<Map<String, Object>> newsList = newsService.searchNews(query);

        if (newsList.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(List.of())); // 빈 리스트 반환
        }

        return ResponseEntity.ok(ApiResponse.success(newsList));
    }

    @PostMapping("/scrap")
    public ResponseEntity<ApiResponse<Void>> scrapNews(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newsId = request.get("newsId");

        // 스크랩 처리 로직
        newsService.scrapNews(email, newsId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 스크랩한 뉴스 삭제
    @DeleteMapping("/scrap/{newsId}")
    public ResponseEntity<ApiResponse<Void>> deleteScrapNews(@PathVariable String newsId, @RequestBody Map<String, String> request) {
        String email = request.get("email");

        // 스크랩 삭제 처리
        newsService.deleteScrapNews(email, newsId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 스크랩한 뉴스 상태 확인
    @GetMapping("/scrap/status/{newsId}")
    public ResponseEntity<ApiResponse<Boolean>> checkScrapStatus(@PathVariable String newsId, @RequestParam String email) {
        boolean isScrapped = newsService.isNewsScrapped(email, newsId);
        return ResponseEntity.ok(ApiResponse.success(isScrapped));
    }




}
