package com.JH.JhOnlineJudge.domain.oauth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class NaverUserInfo implements OAuthUserInfo{
    private final String id;
    private final String email;
    private final String nickname;

    public NaverUserInfo(Map<String, Object> responseMap) {
        this.id = (String) responseMap.get("id");
        this.nickname = (String) responseMap.get("nickname");
        this.email = (String) responseMap.get("email");
    }

    public OAuthProvider getProvider() { return OAuthProvider.NAVER; }

}
