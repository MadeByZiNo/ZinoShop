package com.JH.JhOnlineJudge.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Stack;
import java.util.UUID;

@Slf4j
@Component
public class LogTracer {

    private static final ThreadLocal<Integer> traceStep = ThreadLocal.withInitial(() -> 0);
    private static final ThreadLocal<String> uuid = ThreadLocal.withInitial(() -> UUID.randomUUID().toString().substring(0, 12));
    private static final ThreadLocal<Stack<Long>> startTime = ThreadLocal.withInitial(Stack::new);

    public static void nextStep() {
        traceStep.set(traceStep.get() + 1);
    }

    public static void prevStep() {
        traceStep.set(traceStep.get() - 1);
    }

    public static void logStart(String methodName) {
        int step = traceStep.get();
        startTime.get().push(System.currentTimeMillis());

        String indent = "  ".repeat(step - 1); // 들여쓰기 (1부터 시작)
        String prefix = (step == 1) ? "[▶]" : indent + "└─▶";

        String traceId = uuid.get();
        MDC.put("traceId", traceId);

        log.info("{} {}", prefix, methodName);
    }

    public static void logEnd(String methodName) {
        int step = traceStep.get();
        Stack<Long> stack = startTime.get();

        if (stack.isEmpty()) {
            log.warn("⚠ {} 실행 시간 측정 실패 ", methodName);
            return;
        }

        long executionTime = System.currentTimeMillis() - stack.pop();
        String indent = "  ".repeat(step - 1);
        String prefix = (step == 1) ? "[✔]" : indent + "◀─✔";

        log.info("{} {} ({} ms)",prefix, methodName, executionTime);
    }

    public static void logException(String methodName, String message) {
        int step = traceStep.get();
        String indent = "  ".repeat(step - 1);
        String prefix = indent + "❌";

        log.error("{} {} exception: {}", prefix, methodName, message);
    }

    private void clear() {
        MDC.clear();
        traceStep.remove();
        uuid.remove();
        startTime.remove();
    }
    public void clearIfDone(){
        if (traceStep.get() == 0) {
            clear();
        }
    }
}
