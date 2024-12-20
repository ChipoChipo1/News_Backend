package com.news.backend.service;

import com.news.backend.domain.User;
import com.news.backend.dto.OAuthUserInfo;
import com.news.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
}