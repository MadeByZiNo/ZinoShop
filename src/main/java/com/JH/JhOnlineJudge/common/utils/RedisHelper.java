package com.JH.JhOnlineJudge.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisHelper{

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;


    public <T> Optional<T> getData(String key, Class<T> classType) {
        String stringData = (String) redisTemplate.opsForValue().get(key);
        try {
            if (StringUtils.hasText(stringData)) {
                return Optional.ofNullable(objectMapper.readValue(stringData, classType));
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Redis 읽기 실패 (key: {})", key, e);
            return Optional.empty();
        }
    }

    public <T> void saveData(String key, T data, Duration timeout) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            System.out.println(jsonData);
            redisTemplate.opsForValue().set(key, jsonData, timeout);
        } catch (JsonProcessingException e) {
            log.error("Redis 저장 JSON 직렬화 오류 (key: {})", key, e);
        } catch (Exception e) {
            log.error("Redis 저장 실패 (key: {})", key, e);
        }
    }

    // 리스트 데이터 가져오기
    public <T> List<T> getListData(String key, Class<T> classType) {
        List<String> rawList = redisTemplate.opsForList().range(key, 0, -1);
        if (rawList == null) return new ArrayList<>();

        return rawList.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, classType);
                    } catch (JsonProcessingException e) {
                        log.error("리스트 내부 JSON 파싱 오류 (key: {})", key, e);
                        return null;
                    }
                })
                .filter(item -> item != null)
                .collect(Collectors.toList());
    }

    // 리스트 저장 (기존 key 삭제 후 다시 삽입)
    public <T> void saveListData(String key, List<T> data, Duration timeout) {
        redisTemplate.delete(key);
        data.forEach(item -> {
            try {
                String json = objectMapper.writeValueAsString(item);
                redisTemplate.opsForList().rightPush(key, json);
            } catch (JsonProcessingException e) {
                log.error("리스트 저장 JSON 직렬화 오류 (key: {})", key, e);
            }
        });
        redisTemplate.expire(key, timeout);
    }

    // 기존 리스트에 데이터 1개 추가
    public <T> void saveDataToList(String key, T item) {
        try {
            String json = objectMapper.writeValueAsString(item);
            redisTemplate.opsForList().rightPush(key, json);
        } catch (JsonProcessingException e) {
            log.error("리스트 항목 추가 실패 (key: {})", key, e);
        }
    }

    // 기존 리스트에 데이터 1개 추가
    public <T> void saveDataToList(String key, T item, int max) {
        try {
            String json = objectMapper.writeValueAsString(item);
            redisTemplate.opsForList().rightPush(key, json);
            redisTemplate.opsForList().trim(key,0,max);
        } catch (JsonProcessingException e) {
            log.error("리스트 항목 추가 실패 (key: {})", key, e);
        }
    }

    // 패턴 기반 key 가져오기
    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    // key 삭제
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }


    public void publishData(String topic, Object data) {
        try {
            String json = objectMapper.writeValueAsString(data);
            redisTemplate.convertAndSend(topic, json);
        } catch (JsonProcessingException e) {
            log.error("Redis JSON 직렬화 실패 (topic: {})", topic, e);
        } catch (Exception e) {
            log.error("Redis convertAndSend 실패 (topic: {})", topic, e);
        }
    }


    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }
}
