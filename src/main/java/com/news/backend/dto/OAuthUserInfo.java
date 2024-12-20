package com.news.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthUserInfo {
    private String provider;
    private String oauthId;
    private String email;
    private String nickname;
    private String profileImageUrl;

}
