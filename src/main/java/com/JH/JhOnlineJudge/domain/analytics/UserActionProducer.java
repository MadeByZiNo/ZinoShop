package com.JH.JhOnlineJudge.domain.analytics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserActionProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void logProductClick(Long productId) {

        try {
            kafkaTemplate.send("user-action-log", productId);
        } catch (Exception e) {
            log.warn("Kafka 전송 실패  productId: {}, error: {}", productId, e.getMessage());
        }
    }

  /*  public void logSearchKeyword(Long userId, String keyword) {
        JSONObject logJson = new JSONObject();
        logJson.put("event", "SEARCH");
        logJson.put("userId", userId);
        logJson.put("keyword", keyword);
        logJson.put("timestamp", Instant.now().toString());

        kafkaTemplate.send("user-action-log", logJson.toString());
    }*/
}
