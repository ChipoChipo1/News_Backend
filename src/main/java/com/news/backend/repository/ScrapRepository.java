package com.news.backend.repository;

import com.news.backend.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, String> {

    // 사용자의 뉴스 스크랩 정보 확인
    boolean existsByEmailAndNewsId(String email, String newsId);

    // 사용자의 뉴스 스크랩 삭제
    void deleteByEmailAndNewsId(String email, String newsId);
}