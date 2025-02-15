package com.JH.JhOnlineJudge.common.aop;

import com.JH.JhOnlineJudge.common.utils.LogTracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final LogTracer logTracer;

    // 메서드 시작 시점에 로그 시작
    @Before("execution(* com.JH.JhOnlineJudge..controller..*(..)) " +
            "|| execution(* com.JH.JhOnlineJudge..service..*(..)) " +
            "|| execution(* com.JH.JhOnlineJudge..repository..*(..))")
    public void logMethodStart(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();

        LogTracer.nextStep();
        LogTracer.logStart(methodName);
    }

    // 메서드 실행 종료 시점에 로그 종료
    @After("execution(* com.JH.JhOnlineJudge..controller..*(..)) " +
            "|| execution(* com.JH.JhOnlineJudge..service..*(..)) " +
            "|| execution(* com.JH.JhOnlineJudge..repository..*(..))")
    public void logMethodEnd(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        long executionTime = System.currentTimeMillis();
        LogTracer.logEnd(methodName, executionTime);
        LogTracer.prevStep();  // 단계 감소
    }

    // 예외 발생 시 로깅
    @AfterThrowing(value = "execution(* com.JH.JhOnlineJudge..controller..*(..)) " +
            "|| execution(* com.JH.JhOnlineJudge..service..*(..)) " +
            "|| execution(* com.JH.JhOnlineJudge..repository..*(..))", throwing = "throwable")
    public void logException(JoinPoint joinPoint, Throwable throwable) {
        String methodName = joinPoint.getSignature().toShortString();
        LogTracer.logException(methodName, throwable.getMessage());
    }

}
