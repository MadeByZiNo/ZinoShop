package com.JH.JhOnlineJudge.common.utils;

import groovyjarjarpicocli.CommandLine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class LogTracer {


    private static ThreadLocal<Integer> traceStep = ThreadLocal.withInitial(() -> 0);  // 단계
    private static ThreadLocal<String> uuid = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());  // UUID

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

        // 1단계일 때는 화살표 없이 출력
        if (step == 1) {
            log.info("[{}] :: ({}) {}", getUuid(), step, methodName);
        } else {
            log.info("[{}] :: {}> ({}) {}", getUuid(), indent, step, methodName);
        }
    }

    public static void logEnd(String methodName, long executionTime) {
        int step = traceStep.get();
        String indent = "=".repeat(step * 2);  // 단계에 따라 == 두 배씩 증가

        // 1단계일 때는 화살표 없이 출력
        if (step == 1) {
            log.info("[{}] :: ({}) {} executed in {} ms", getUuid(), step, methodName, executionTime);
        } else {
            log.info("[{}] :: <{} ({}) {} executed in {} ms", getUuid(), indent,  step,  methodName, executionTime);
        }

    }

    public static void logException(String methodName, String message) {
        int step = traceStep.get();
        String indent = "=".repeat(step * 2);  // 단계에 따라 == 두 배씩 증가
        log.error("[{}] :: X{} ({}) {} exception: {}", getUuid(), indent,  step,  methodName, message);
    }
}
