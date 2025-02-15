package com.JH.JhOnlineJudge.user.domain;

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

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthUserResolver implements HandlerMethodArgumentResolver {

     private final UserRepository userRepository;
     private final JwtUtil jwtUtil;

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

        if(role.equals("NOT_LOGIN")){
            throw new LoginInvalidException();
        }

        String username =  (String) request.getAttribute("username");
        User user = userRepository.findByUsername(username)
                .orElseThrow(LoginInvalidException::new);
        log.info("authentication userId: {}", user.getId());
        return user.getId();

    }
}
