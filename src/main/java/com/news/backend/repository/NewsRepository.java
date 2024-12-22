package com.news.backend.repository;


import com.news.backend.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface NewsRepository extends JpaRepository<News, String> {

    // 뉴스 제목에 검색어가 포함된 뉴스 찾기 (간단한 예시)
    List<News> findByTitleContaining(String query);
}