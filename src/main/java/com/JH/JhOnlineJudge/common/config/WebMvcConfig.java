package com.JH.JhOnlineJudge.common.config;

import com.JH.JhOnlineJudge.domain.user.admin.domain.AdminResolver;
import com.JH.JhOnlineJudge.domain.user.auth.AuthUserResolver;
import com.JH.JhOnlineJudge.domain.user.auth.IsLoginResolver;
import com.JH.JhOnlineJudge.domain.user.interceptor.TokenCheckInterceptor;
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
                .addPathPatterns("/**")
                .excludePathPatterns(// 특정 경로는 제외
                "notification/*",
                "/css/**",                             // 로그인 페이지
                "/images/**",                        // 이미지, CSS, JS 등
                "https://code.jquery.com/**",        // CDN 주소 제외
                "https://cdn.jsdelivr.net/**"
                );
    }

}
