package com.news.backend.service;

import com.news.backend.domain.Keyword;
import com.news.backend.domain.User;
import com.news.backend.dto.OAuthUserInfo;
import com.news.backend.repository.KeywordRepository;
import com.news.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;

    // 사용자를 새로 저장하거나 갱신하는 메소드
    public User saveOrUpdateUser(OAuthUserInfo userInfo, String refreshToken) {
        return userRepository.findByOauthIdAndProvider(userInfo.getOauthId(), userInfo.getProvider())
                .map(user -> {
                    user.setRefreshToken(refreshToken);
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .oauthId(userInfo.getOauthId())
                            .provider(userInfo.getProvider())
                            .email(userInfo.getEmail())
                            .nickname(userInfo.getNickname())
                            .profileUrl(userInfo.getProfileImageUrl())
                            .build();
                    return userRepository.save(newUser);
                });
    }

    // 사용자의 프로필 정보 수정
    public void updateUserProfile(Map<String, String> updatedProfile) {
        // 이메일을 이용해 사용자를 찾고, 프로필을 업데이트
        String email = updatedProfile.get("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        user.setNickname(updatedProfile.get("nickname"));
        user.setProfileUrl(updatedProfile.get("profileUrl"));

        userRepository.save(user);
    }

    // 사용자의 관심 키워드 설정
    public void setUserKeywords(List<String> keywords, String email) {
        for (String keyword : keywords) {
            if (!keywordRepository.existsByEmailAndKeyword(email, keyword)) {
                Keyword newKeyword = new Keyword(email, keyword, System.currentTimeMillis(), System.currentTimeMillis());
                keywordRepository.save(newKeyword);
            }
        }
    }

    // 사용자의 관심 키워드 조회
    public List<Keyword> getUserKeywords(String email) {
        return keywordRepository.findByEmail(email);
    }

    // 사용자의 관심 키워드 삭제
    public void deleteUserKeyword(String email, String keyword) {
        keywordRepository.deleteByEmailAndKeyword(email, keyword);
    }

    // 사용자의 프로필 정보 조회
    public Map<String, Object> getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자 프로필 정보 반환
        return Map.of(
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "profileUrl", user.getProfileUrl()
        );
    }
}