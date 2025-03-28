package com.JH.JhOnlineJudge.domain.user.admin.domain;

import com.JH.JhOnlineJudge.domain.user.dto.UserTokenDto;
import com.JH.JhOnlineJudge.domain.user.exception.AccessDeniedException;
import com.JH.JhOnlineJudge.domain.user.service.TokenService;
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
public class AdminResolver implements HandlerMethodArgumentResolver {

    private final TokenService tokenService;

    @Override
     public boolean supportsParameter(MethodParameter parameter) {
          return  parameter.hasParameterAnnotation(Admin.class) &&
                  parameter.getParameterType().equals(Long.class);
     }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        String accessToken = accessTokenCookie.getValue();

        UserTokenDto userTokenDto = tokenService.getAuthInfo(accessToken);

        if (!userTokenDto.getRole().name().equals("관리자")) {
            throw new AccessDeniedException(); // 관리자가 아닐 때
        }

        log.info("관리자 계정 로그인: {}", userTokenDto.getUserId());
        return userTokenDto.getUserId();

    }
}
