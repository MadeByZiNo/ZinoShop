package com.JH.JhOnlineJudge.common.aop;

import com.JH.JhOnlineJudge.domain.analytics.UserActionProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserActionLoggerAspect {

    private final UserActionProducer userActionProducer;

    @Pointcut("execution(* com.JH.JhOnlineJudge.domain.product.controller.ProductController.getProductPage(..))")
    public void productViewPointcut() {}

    @AfterReturning("productViewPointcut()")
    public void logProductView(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long productId = (Long) args[0];

        if (productId != null) {
            userActionProducer.logProductClick(productId);
        }
    }

  /*  @AfterReturning("searchKeywordPointcut()")
    public void logSearchKeyword(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String keyword = extractKeyword(args);
        Long userId = getUserIdFromContext();

        if (userId != null && keyword != null && !keyword.isBlank()) {
            userActionLogger.logSearchKeyword(userId, keyword);
        }
    } */

    private Long extractProductId(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> arg instanceof Long)
                .map(arg -> (Long) arg)
                .findFirst().orElse(null);
    }

    private String extractKeyword(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> arg instanceof String)
                .map(arg -> (String) arg)
                .findFirst().orElse(null);
    }

}
