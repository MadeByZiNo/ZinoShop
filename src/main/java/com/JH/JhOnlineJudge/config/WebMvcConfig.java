package com.JH.JhOnlineJudge.config;

import com.JH.JhOnlineJudge.user.domain.AuthUserResolver;
import com.JH.JhOnlineJudge.user.interceptor.AuthorityCheckInterceptor;
import com.JH.JhOnlineJudge.user.interceptor.TokenCheckInterceptor;
import com.JH.JhOnlineJudge.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final TokenCheckInterceptor tokenCheckInterceptor;
    private final AuthorityCheckInterceptor authorityCheckInterceptor;
    private final AuthUserResolver authUserResolver;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserResolver);
    }

    @Bean
      public RestTemplate restTemplate() {
          return new RestTemplate();
      }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenCheckInterceptor)      // 토큰 체크 및 재발급
                .order(1)
                .addPathPatterns("/**");
                //.excludePathPatterns("/public/**");

        registry.addInterceptor(authorityCheckInterceptor)      // 권한 확인 후 처리
                .order(2)
                .addPathPatterns("/board/write", "/purchase", "/review");
    }

}
