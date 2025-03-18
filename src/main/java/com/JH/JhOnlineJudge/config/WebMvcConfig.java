package com.JH.JhOnlineJudge.config;

import com.JH.JhOnlineJudge.user.admin.domain.AdminResolver;
import com.JH.JhOnlineJudge.user.domain.AuthUserResolver;
import com.JH.JhOnlineJudge.user.domain.IsLoginResolver;
import com.JH.JhOnlineJudge.user.interceptor.AuthorityCheckInterceptor;
import com.JH.JhOnlineJudge.user.interceptor.TokenCheckInterceptor;
import com.JH.JhOnlineJudge.common.utils.JwtUtil;
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
    private final AdminResolver adminResolver;
    private final IsLoginResolver isLoginResolver;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserResolver);
        resolvers.add(adminResolver);
        resolvers.add(isLoginResolver);
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

    }

}
