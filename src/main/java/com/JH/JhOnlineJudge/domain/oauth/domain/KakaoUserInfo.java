package com.JH.JhOnlineJudge.domain.oauth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class KakaoUserInfo implements OAuthUserInfo{
    private final String id;
    private final String email;
    private final String nickname;

    public KakaoUserInfo(Map<String, Object> body) {
        this.id = String.valueOf(body.get("id"));
        Map<String, Object> account = (Map<String, Object>) body.get("kakao_account");
        this.nickname = (String) ((Map<String, Object>) account.get("profile")).get("nickname");
        this.email = (String) account.get("email");
    }

    public OAuthProvider getProvider() { return OAuthProvider.KAKAO; }

}
