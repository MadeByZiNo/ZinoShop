package com.JH.JhOnlineJudge.common.utils;

import groovyjarjarpicocli.CommandLine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Stack;
import java.util.UUID;

@Slf4j
@Component
public class LogTracer {


    private static ThreadLocal<Integer> traceStep = ThreadLocal.withInitial(() -> 0);  // 단계
    private static ThreadLocal<String> uuid = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());  // UUID
    private static final ThreadLocal<Stack<Long>> startTime = ThreadLocal.withInitial(Stack::new);  // 실행 시작 시간 저장
    public static void prevStep() {
        traceStep.set(traceStep.get() - 1);
    }

    public static void nextStep() {
        traceStep.set(traceStep.get() + 1);
    }

    public static String getUuid() {
        return uuid.get();
    }

    public static void logStart(String methodName) {
        int step = traceStep.get();
        String indent = "=".repeat(step * 2);  // 단계에 따라 == 두 배씩 증가
        startTime.get().push(System.currentTimeMillis());  // 실행 시작 시간 기록

        // 1단계일 때는 화살표 없이 출력
        if (step == 1) {
            log.info("Thread : [{}] :: ({}) {}", getUuid(), step, methodName);
        } else {
            log.info("Thread : [{}] :: {}> ({}) {}", getUuid(), indent, step, methodName);
        }
    }

    public static void logEnd(String methodName) {
        int step = traceStep.get();
        String indent = "=".repeat(step * 2);  // 단계에 따라 == 두 배씩 증가

        Stack<Long> stack = startTime.get();

        if (stack.isEmpty()) {
            log.warn("Thread : [{}] :: ({}) {} 실행 시간 측정 실패 (스택이 비어 있음)", getUuid(), step, methodName);
            return;
        }

        long executionTime = System.currentTimeMillis() -  stack.pop(); // 실행 시간 계산

        // 1단계일 때는 화살표 없이 출력
        if (step == 1) {
            log.info("Thread : [{}] :: ({}) {} executed in {} ms", getUuid(), step, methodName, executionTime);
        } else {
            log.info("Thread : [{}] :: <{} ({}) {} executed in {} ms", getUuid(), indent,  step,  methodName, executionTime);
        }

    }

    public static void logException(String methodName, String message) {
        int step = traceStep.get();
        String indent = "=".repeat(step * 2);  // 단계에 따라 == 두 배씩 증가
        log.error("Thread : [{}] :: X{} ({}) {} exception: {}", getUuid(), indent,  step,  methodName, message);
    }
}
