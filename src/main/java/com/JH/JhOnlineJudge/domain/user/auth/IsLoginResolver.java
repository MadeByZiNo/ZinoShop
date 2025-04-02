package com.JH.JhOnlineJudge.domain.user.auth;

import com.JH.JhOnlineJudge.domain.user.dto.IsLoginRequest;
import com.JH.JhOnlineJudge.domain.user.dto.UserTokenDto;
import com.JH.JhOnlineJudge.common.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class IsLoginResolver implements HandlerMethodArgumentResolver {

    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return  parameter.hasParameterAnnotation(IsLogin.class) &&
        parameter.getParameterType().equals(IsLoginRequest.class);

    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String role = (String) request.getAttribute("userStatus");

        IsLoginRequest isLoginRequest = new IsLoginRequest();

        if(role.equals("NOT_LOGIN")){
            isLoginRequest.setIsLogin(false);
            return isLoginRequest;
        }

        // ✅ Redis에서 유저 정보 가져오기
        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        String accessToken = accessTokenCookie.getValue();
        UserTokenDto userTokenDto = tokenService.getAuthInfo(accessToken);
        log.info("authentication userId: {}", userTokenDto.getUserId());

        isLoginRequest.setIsLogin(true);
        isLoginRequest.setUserId(userTokenDto.getUserId());
        return isLoginRequest;
    }

}
