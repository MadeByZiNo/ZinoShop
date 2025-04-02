package com.JH.JhOnlineJudge.domain.oauth.service;

import com.JH.JhOnlineJudge.domain.oauth.domain.OAuthProvider;
import com.JH.JhOnlineJudge.domain.oauth.domain.OAuthUserInfo;

public interface OAuthService {
    OAuthProvider getProvider();
    String getAccessToken(String code, String state);
    OAuthUserInfo getUserInfo(String accessToken);
}
