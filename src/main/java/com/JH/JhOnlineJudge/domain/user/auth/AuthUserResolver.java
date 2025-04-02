package com.JH.JhOnlineJudge.domain.user.auth;

import com.JH.JhOnlineJudge.domain.user.dto.UserTokenDto;
import com.JH.JhOnlineJudge.domain.user.exception.LoginInvalidException;
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
public class AuthUserResolver implements HandlerMethodArgumentResolver {

    private final TokenService tokenService;

    @Override
     public boolean supportsParameter(MethodParameter parameter) {
          return  parameter.hasParameterAnnotation(AuthUser.class) &&
                  parameter.getParameterType().equals(Long.class);
     }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String role = (String) request.getAttribute("userStatus");

        // 인터셉터를 거쳐서 로그인 상태가 아니라면 인가 불가능
        if(role.equals("NOT_LOGIN")){
            throw new LoginInvalidException();
        }


        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        String accessToken = accessTokenCookie.getValue();

        // ✅ Redis에서 바로 user 정보 가져오기
        UserTokenDto userTokenDto = tokenService.getAuthInfo(accessToken);
        log.info("authentication userId: {}", userTokenDto.getUserId());
        return userTokenDto.getUserId();

    }
}
