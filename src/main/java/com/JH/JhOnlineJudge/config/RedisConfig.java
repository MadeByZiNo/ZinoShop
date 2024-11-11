package com.JH.JhOnlineJudge.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;


    @Bean
      public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
      }


      @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setEnableTransactionSupport(false);
        redisTemplate.setKeySerializer(new StringRedisSerializer());


        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        redisTemplate.setValueSerializer(serializer);

        return redisTemplate;
      }
}
