package com.JH.JhOnlineJudge.common.aop;

import com.JH.JhOnlineJudge.common.utils.LogTracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ExecutionTracingLoggerAspect {

    private final LogTracer logTracer;

    // 메서드 시작 시점에 로그 시작
    @Before("@within(org.springframework.stereotype.Service) " +
            "|| @within(org.springframework.stereotype.Repository) " +
            "|| @within(org.springframework.stereotype.Controller)" +
            "|| @within(org.springframework.web.bind.annotation.RestController)")
    public void logMethodStart(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();

        LogTracer.nextStep();
        LogTracer.logStart(methodName);
    }

    // 메서드 실행 종료 시점에 로그 종료
    @After( "@within(org.springframework.stereotype.Service) " +
            "|| @within(org.springframework.stereotype.Repository) " +
            "|| @within(org.springframework.stereotype.Controller)" +
            "|| @within(org.springframework.web.bind.annotation.RestController)")
    public void logMethodEnd(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        LogTracer.logEnd(methodName);
        LogTracer.prevStep();  // 단계 감소

        logTracer.clearIfDone();
    }

    // 예외 발생 시 로깅
    @AfterThrowing(value = "@within(org.springframework.stereotype.Service) " +
            "|| @within(org.springframework.stereotype.Repository) " +
            "|| @within(org.springframework.stereotype.Controller)" +
            "|| @within(org.springframework.web.bind.annotation.RestController)", throwing = "throwable")
    public void logException(JoinPoint joinPoint, Throwable throwable) {
        String methodName = joinPoint.getSignature().toShortString();
        LogTracer.logException(methodName, throwable.getMessage());
    }

}
