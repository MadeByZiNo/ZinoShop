package com.JH.JhOnlineJudge.domain.user.interceptor;

import com.JH.JhOnlineJudge.domain.user.entity.UserRole;
import com.JH.JhOnlineJudge.common.utils.JwtUtil;
import com.JH.JhOnlineJudge.domain.user.dto.UserTokenDto;
import com.JH.JhOnlineJudge.domain.user.auth.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCheckInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        Cookie refreshTokenCookie = WebUtils.getCookie(request, "refreshToken");

        // 쿠키가 존재하지 않을 경우 비로그인
        if (accessTokenCookie == null || refreshTokenCookie == null) {
            request.setAttribute("userStatus", "NOT_LOGIN");
            return true;
        }


        String accessToken = accessTokenCookie.getValue();
        String refreshToken = refreshTokenCookie.getValue();

        if(jwtUtil.isInvalidToken(accessTokenCookie.getValue())){
            if(jwtUtil.isInvalidToken(refreshTokenCookie.getValue())){     // 둘 다 만료되었으면 비로그인
                request.setAttribute("userStatus","NOT_LOGIN");
                return true;
            }

            // Redis에서 refreshToken 검증
            Long userId = jwtUtil.getUserId(refreshToken); // jwt에 userId 포함 시켜야 함
            String storedRefreshToken = tokenService.getRefreshToken(userId);
            if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
                request.setAttribute("userStatus", "NOT_LOGIN");
                return true;
            }


            // 엑세스만 만료되어있고 리프레시는 살아있다면 엑세스 토큰 재발급
            String username = jwtUtil.getUsername(refreshToken);
            String nickname = jwtUtil.getNickname(refreshToken);
            UserRole role = jwtUtil.getRole(refreshToken);

            accessToken = jwtUtil.generateAccessToken(username, nickname, role, userId);
            refreshToken = jwtUtil.generateRefreshToken(username, nickname, role, userId);

            // redis에 저장
            UserTokenDto userTokenDto = UserTokenDto.of(userId, username, nickname, role);
            tokenService.saveAuthInfo(accessToken, userTokenDto);
            tokenService.saveRefreshToken(refreshToken, userId);

            addJwtToken(response, accessToken, refreshToken);
        }

        UserTokenDto authInfo = tokenService.getAuthInfo(accessToken);
        if (authInfo == null) {
            request.setAttribute("userStatus", "NOT_LOGIN");
            return true;
        }
        request.setAttribute("username", authInfo.getUsername());
        request.setAttribute("nickname",authInfo.getNickname());
        request.setAttribute("userStatus", authInfo.getRole().name());
        request.setAttribute("userId", authInfo.getUserId());

        return true;
    }

    @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            String nickname = (String) request.getAttribute("nickname");
            modelAndView.addObject("nickname",nickname);
        }
    }


    private static void addJwtToken(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(true);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        response.addCookie(refreshCookie);
    }
}
