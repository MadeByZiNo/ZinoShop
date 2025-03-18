package com.JH.JhOnlineJudge.user.domain;

import com.JH.JhOnlineJudge.user.dto.IsLoginRequest;
import com.JH.JhOnlineJudge.user.exception.LoginInvalidException;
import com.JH.JhOnlineJudge.user.repository.UserRepository;
import com.JH.JhOnlineJudge.common.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class IsLoginResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
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

        String username =  (String) request.getAttribute("username");
        User user = userRepository.findByUsername(username)
                .orElseThrow(LoginInvalidException::new);
        log.info("authentication userId: {}", user.getId());

        isLoginRequest.setIsLogin(true);
        isLoginRequest.setUserId(user.getId());
        return isLoginRequest;
    }

}
