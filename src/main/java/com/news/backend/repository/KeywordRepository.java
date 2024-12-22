package com.news.backend.repository;

import com.news.backend.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    // 특정 사용자의 키워드 목록을 가져오기
    List<Keyword> findByEmail(String email);

    // 특정 사용자의 키워드 존재 여부 확인
    boolean existsByEmailAndKeyword(String email, String keyword);

    // 특정 사용자의 키워드 삭제
    void deleteByEmailAndKeyword(String email, String keyword);
}
