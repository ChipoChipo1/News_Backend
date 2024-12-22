package com.news.backend.service;

import com.news.backend.domain.Keyword;
import com.news.backend.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    // 사용자의 키워드 설정
    public void setUserKeywords(List<String> keywords, String email) {
        for (String keyword : keywords) {
            // 이미 등록된 키워드는 중복 삽입하지 않도록 체크
            if (!keywordRepository.existsByEmailAndKeyword(email, keyword)) {
                Keyword newKeyword = new Keyword(email, keyword, System.currentTimeMillis(), System.currentTimeMillis());
                keywordRepository.save(newKeyword);
            }
        }
    }

    // 사용자의 키워드 조회
    public List<Keyword> getUserKeywords(String email) {
        return keywordRepository.findByEmail(email);
    }

    // 사용자의 키워드 삭제
    public void deleteUserKeyword(String email, String keyword) {
        keywordRepository.deleteByEmailAndKeyword(email, keyword);
    }
}
