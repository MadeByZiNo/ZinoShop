package com.JH.JhOnlineJudge.user.admin.domain;

import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.exception.AccessDeniedException;
import com.JH.JhOnlineJudge.user.exception.LoginInvalidException;
import com.JH.JhOnlineJudge.user.repository.UserRepository;
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

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminResolver implements HandlerMethodArgumentResolver {

     private final UserRepository userRepository;
     private final JwtUtil jwtUtil;

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

        Cookie[] cookies = ((HttpServletRequest) webRequest.getNativeRequest()).getCookies();
        if(cookies == null) {
            throw new LoginInvalidException();
        }

        Cookie token = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("accessToken"))
                       .findAny()
                       .orElseThrow(LoginInvalidException::new);

        if(jwtUtil.isInvalidToken(token.getValue())) { throw new LoginInvalidException();}
        String username = jwtUtil.getUsername(token.getValue());
        User user = userRepository.findByUsername(username)
                .orElseThrow(LoginInvalidException::new);
        if(user.getRole().equals("관리자")){
            throw new AccessDeniedException();
        }
        log.info("관리자 계정 로그인: {}", user.getId());
        return user.getId();

    }
}
