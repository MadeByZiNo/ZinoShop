package com.JH.JhOnlineJudge.user.interceptor;

import com.JH.JhOnlineJudge.user.domain.UserRole;
import com.JH.JhOnlineJudge.common.utils.JwtUtil;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        Cookie refreshTokenCookie = WebUtils.getCookie(request, "refreshToken");

        // 쿠키가 존재하지 않을 경우
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

            // 엑세스만 만료되어있으면 토큰 재발급
            String username = jwtUtil.getUsername(refreshToken);
            String nickname = jwtUtil.getNickname(refreshToken);
            UserRole role = jwtUtil.getRole(refreshToken);
            accessToken = jwtUtil.generateAccessToken(username, nickname, role);
            refreshToken = jwtUtil.generateRefreshToken(username, nickname, role);

            addJwtToken(response, accessToken, refreshToken);
        }

        request.setAttribute("username", jwtUtil.getUsername(refreshToken));
        request.setAttribute("nickname",jwtUtil.getNickname(refreshToken));
        request.setAttribute("userStatus", jwtUtil.getRole(refreshToken).name());
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
