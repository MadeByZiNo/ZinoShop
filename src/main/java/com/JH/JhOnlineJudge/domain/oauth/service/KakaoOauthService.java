package com.JH.JhOnlineJudge.domain.oauth.service;

import com.JH.JhOnlineJudge.domain.oauth.domain.OAuthProvider;
import com.JH.JhOnlineJudge.domain.oauth.domain.KakaoUserInfo;
import com.JH.JhOnlineJudge.domain.oauth.domain.OAuthUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class KakaoOauthService  implements OAuthService {

    @Value("${oauth.kakao.client_id}")
    private String KAKAO_CLIENT_ID;

    @Value("${oauth.kakao.url}")
    private final String KAKAO_REDIRECT_URL = "http://localhost:8080/oauth/kakao/callback";

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String getAccessToken(String code, String state) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.ACCEPT_CHARSET, "utf-8"); // 선택사항 (인코딩 지정)

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_uri", KAKAO_REDIRECT_URL);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        return (String) response.getBody().get("access_token");
    }

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        String userInfoUrl = UriComponentsBuilder
                .fromHttpUrl("https://kapi.kakao.com/v2/user/me")
                .build()
                .toUriString();

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                entity,
                Map.class
        );

        return new KakaoUserInfo(response.getBody());
    }
}
