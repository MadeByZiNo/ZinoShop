package com.JH.JhOnlineJudge.domain.oauth.service;

import com.JH.JhOnlineJudge.domain.oauth.domain.OAuthProvider;
import com.JH.JhOnlineJudge.domain.oauth.domain.NaverUserInfo;
import com.JH.JhOnlineJudge.domain.oauth.domain.OAuthUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class NaverOauthService implements OAuthService {

    @Value("${oauth.naver.client_id}")
    private String NAVER_CLIENT_ID;

    @Value("${oauth.naver.client_secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${oauth.naver.url}")
    private final String NAVER_REDIRECT_URL = "http://localhost:8080/oauth/naver/callback";

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public String getAccessToken(String code, String state) {
        String tokenUrl = "https://nid.naver.com/oauth2.0/token";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", NAVER_CLIENT_ID)
                .queryParam("client_secret", NAVER_CLIENT_SECRET)
                .queryParam("code", code)
                .queryParam("state", state);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(builder.toUriString(), Map.class);
        return (String) response.getBody().get("access_token");
    }

    @Override
    public OAuthUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        String userInfoUrl = UriComponentsBuilder
                .fromHttpUrl("https://openapi.naver.com/v1/nid/me")
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

        Map<String, Object> responseMap = (Map<String, Object>) response.getBody().get("response");

        return new NaverUserInfo(responseMap);
    }
}
