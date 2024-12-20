package com.news.backend.service;

import com.news.backend.dto.OAuthUserInfo;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OAuthService {

    public OAuthUserInfo getUserInfo(OAuth2AuthenticationToken token) {
        Map<String, Object> attributes = token.getPrincipal().getAttributes();
        String provider = token.getAuthorizedClientRegistrationId().toUpperCase();

        switch (provider) {
            case "KAKAO":
                return extractKakaoUser(attributes);
            /*case "GITHUB":
                return extractGitHubUser(attributes);*/
            default:
                throw new IllegalArgumentException("지원하지 않는 공급자입니다.");
        }
    }

/* 응답 예시
    {
        "id": "1234567890",
            "kakao_account": {
        "email": "user@kakao.com",
                "profile": {
            "nickname": "홍길동",
                    "profile_image_url": "https://example.com/profile_image.png"
        }
    }
    }
*/

    private OAuthUserInfo extractKakaoUser(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        //만약 이메일이 존재하지않을시에 해당 값으로 지정함
        String email = (String) kakaoAccount.getOrDefault("email", "no_email@kakao.com");
        return new OAuthUserInfo(
                "KAKAO",
                String.valueOf(attributes.get("id")),
                email,
                (String) profile.get("nickname"),
                (String) profile.get("profile_image_url")
        );
    }
/*
    private OAuthUserInfo extractGitHubUser(Map<String, Object> attributes) {

        return new OAuthUserInfo(
                "GITHUB",
                String.valueOf(attributes.get("id")),
                (String) attributes.get("email"),
                (String) attributes.get("login"),
                (String) attributes.get("avatar_url")
        );
    }
    */
}
