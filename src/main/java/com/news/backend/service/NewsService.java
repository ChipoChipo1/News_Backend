package com.news.backend.service;

import com.news.backend.domain.Keyword;
import com.news.backend.domain.News;
import com.news.backend.domain.Scrap;
import com.news.backend.repository.NewsRepository;
import com.news.backend.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final ScrapRepository scrapRepository;
    private final KeywordService keywordService;
    private final StringRedisTemplate redisTemplate;

    // 뉴스 ID로 뉴스 데이터 조회
    public News getNewsById(String newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("뉴스를 찾을 수 없습니다: " + newsId));
    }
    // 뉴스 검색
    public List<Map<String, Object>> searchNews(String query) {
        // 검색어를 기반으로 뉴스 데이터를 검색
        // 예시: 여기서는 간단한 뉴스 제목 검색
        List<News> newsList = newsRepository.findByTitleContaining(query);
        // 뉴스 데이터를 Map<String, Object> 형태로 변환
        return newsList.stream()
                .map(news -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", news.getId());
                    map.put("title", news.getTitle());
                    map.put("content", news.getContent());
                    return map;
                })
                .collect(Collectors.toList());
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
        Double score = redisTemplate.opsForZSet().score(key, newsId);
        return score != null ? score : 0.0; // 조회수 없으면 0.0 반환
    }
    // 뉴스 스크랩
    public void scrapNews(String email, String newsId) {
        // 사용자와 뉴스 아이디를 기반으로 스크랩 저장
        // 1. 뉴스 존재 여부 확인
        Optional<News> news = newsRepository.findById(newsId);
        if (news.isPresent()) {
            // 스크랩 정보 저장 (사용자 이메일과 뉴스 ID)
            scrapRepository.save(new Scrap(email, newsId));
        } else {
            throw new RuntimeException("뉴스를 찾을 수 없습니다.");
        }
    }

    // 스크랩한 뉴스 삭제
    public void deleteScrapNews(String email, String newsId) {
        // 이메일과 뉴스 ID로 스크랩 삭제
        scrapRepository.deleteByEmailAndNewsId(email, newsId);
    }

    // 스크랩한 뉴스 상태 확인
    public boolean isNewsScrapped(String email, String newsId) {
        // 이메일과 뉴스 ID로 스크랩 여부 확인
        return scrapRepository.existsByEmailAndNewsId(email, newsId);
    }
    // 관심 키워드 기반 뉴스 추천
// 관심 키워드 기반 뉴스 추천
    public List<Map<String, String>> getRecommendedNews(String email) {
        // 사용자의 관심 키워드를 가져옴
        List<Keyword> keywords = keywordService.getUserKeywords(email);

        // List<Keyword>를 List<String>으로 변환
        List<String> keywordStrings = keywords.stream()
                .map(Keyword::getKeyword)  // Keyword 객체에서 keyword 값만 추출
                .collect(Collectors.toList());

        // 관심 키워드를 포함하는 뉴스들을 찾음
        List<News> recommendedNews = newsRepository.findAll()
                .stream()
                .filter(news -> containsKeyword(news, keywordStrings))  // List<String> 사용
                .collect(Collectors.toList());

        // 뉴스 목록을 반환
        return recommendedNews.stream()
                .map(news -> Map.of("title", news.getTitle(), "url", news.getUrl())) // Map<String, String> 사용
                .collect(Collectors.toList());
    }

    // 뉴스 제목이나 내용에 키워드가 포함되어 있는지 확인하는 메소드
    private boolean containsKeyword(News news, List<String> keywords) {
        return keywords.stream().anyMatch(keyword ->
                news.getTitle().contains(keyword) ||
                        news.getContent().contains(keyword)
        );
    }

}
