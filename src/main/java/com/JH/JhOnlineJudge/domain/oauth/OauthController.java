package com.JH.JhOnlineJudge.domain.oauth;

import com.JH.JhOnlineJudge.common.utils.JwtUtil;
import com.JH.JhOnlineJudge.domain.oauth.domain.OAuthProvider;
import com.JH.JhOnlineJudge.domain.user.entity.User;
import com.JH.JhOnlineJudge.domain.oauth.domain.OAuthUserInfo;
import com.JH.JhOnlineJudge.domain.oauth.service.OAuthProviderServiceFactory;
import com.JH.JhOnlineJudge.domain.oauth.service.OAuthService;
import com.JH.JhOnlineJudge.domain.user.service.TokenService;
import com.JH.JhOnlineJudge.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {

    @Value("${oauth.naver.client_id}")
    private String NAVER_CLIENT_ID;

    @Value("${oauth.naver.client_secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${oauth.kakao.client_id}")
    private String KAKAO_CLIENT_ID;

    @Value("${oauth.naver.url}")
    private final String NAVER_REDIRECT_URL = "http://localhost:8080/oauth/naver/callback";

    @Value("${oauth.kakao.url}")
    private final String KAKAO_REDIRECT_URL = "http://localhost:8080/oauth/kakao/callback";


    private final OAuthProviderServiceFactory oauthProviderServiceFactory;
    private final UserService userService;
    private final TokenService tokenService;

    @GetMapping("/authorize/naver")
    public void redirectToNaver(HttpServletResponse response) {
        String state = UUID.randomUUID().toString();

        String loginUrl = UriComponentsBuilder.fromHttpUrl("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", NAVER_CLIENT_ID)
                .queryParam("redirect_uri", NAVER_REDIRECT_URL)
                .queryParam("state", state)
                .build()
                .toUriString();

        try {
            response.sendRedirect(loginUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/authorize/kakao")
    public void redirectToKakao(HttpServletResponse response) {
        String state = UUID.randomUUID().toString();

        String loginUrl = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", KAKAO_CLIENT_ID)
                .queryParam("redirect_uri", KAKAO_REDIRECT_URL)
                .queryParam("state", state)
                .build()
                .toUriString();

        try {
            response.sendRedirect(loginUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{provider}/callback")
    public RedirectView handleCallback(@PathVariable String provider, @RequestParam String code, @RequestParam String state, HttpServletResponse response) {
        OAuthProvider oauthProvider = OAuthProvider.valueOf(provider.toUpperCase());
        OAuthService providerService = oauthProviderServiceFactory.getProviderService(oauthProvider);

        String accessToken = providerService.getAccessToken(code, state);
        OAuthUserInfo userInfo = providerService.getUserInfo(accessToken);

        User user = userService.findOrRegister(userInfo);

        ConcurrentHashMap<String, String> tokenMap = tokenService.issueJwt(user);
        JwtUtil.addJwtToken(response, tokenMap.get("accessToken"), tokenMap.get("refreshToken"));

        return new RedirectView("/");
    }

}
