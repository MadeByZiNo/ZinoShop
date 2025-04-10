package com.JH.JhOnlineJudge.domain.analytics;

import com.JH.JhOnlineJudge.common.utils.RedisHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActionConsumer {

    private final RedisHelper redisHelper;

    @KafkaListener(topics = "user-action-log", groupId = "user-action-group")
    public void listen(Long productId) {
        redisHelper.getRedisTemplate().opsForZSet().incrementScore("popular:products", String.valueOf(productId), 1.0);
    }
}
