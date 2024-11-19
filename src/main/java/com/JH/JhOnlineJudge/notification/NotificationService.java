package com.JH.JhOnlineJudge.notification;

import com.JH.JhOnlineJudge.notification.dto.NotificationMessageDto;
import com.JH.JhOnlineJudge.notification.dto.NotificationStatusDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final String keyBase = "notification.";
    private final String keyLast = ".*";

    public NotificationStatusDto getNotificationStatus(Long userId) {
        String keyScan = keyBase + userId + keyLast;
        List<NotificationMessageDto> notifications = getAllNotifications(keyScan);
        long unreadCount = notifications.stream()
                        .filter(notification->!notification.isRead())
                        .count();
        return new NotificationStatusDto(unreadCount > 0, unreadCount);
    }



    public List<NotificationMessageDto> receiveNotificationMessage(Long userId) {
        String keyScan = keyBase + userId + keyLast;
        List<NotificationMessageDto> notifications = getAllNotifications(keyScan);

      return notifications;
    }


    public void setNotificationRead(Long userId) {
        String keyScan = keyBase + userId + keyLast;
        Set<String> keys = redisTemplate.keys(keyScan);
        keys.stream().forEach(key ->{
            List<NotificationMessageDto> notifications = getNotifications(key);                 // key로부터 모든 알림 뽑아오기
            redisTemplate.delete(key);                                                          // 현재 key의 데이터를 clear
            notifications.stream().forEach(notification -> {
                notification.setRead(true);                                                  // 뽑았던 알림들 read처리
                redisTemplate.opsForList().rightPush(key,notification);                         // 뽑았던 알림들 재삽입
            });
            redisTemplate.expire(key, Duration.ofDays(7));
        });
    }


    public void sendNotificationMessage(Long userId, String message, NotificationFrom from) {
        String key = keyBase + userId + from.getFrom();
        NotificationMessageDto notificationMessage = new NotificationMessageDto(message, false, LocalDateTime.now());
        redisTemplate.opsForList().rightPush(key,notificationMessage);
        redisTemplate.expire(key, Duration.ofDays(7));
    }

    private List<NotificationMessageDto> getNotifications(String key) {
          List<NotificationMessageDto> notifications = new ArrayList<>();

          List<Object> result = redisTemplate.opsForList().range(key, 0, -1);
          notifications = (result.stream()
                 .map(notification -> objectMapper.convertValue(notification, NotificationMessageDto.class))
                 .collect(Collectors.toList()));

          return notifications;
        }


    private List<NotificationMessageDto> getAllNotifications(String keyScan) {
        Set<String> keys = redisTemplate.keys(keyScan);

        List<NotificationMessageDto> notifications = new ArrayList<>();
        for (String key : keys) {
           notifications.addAll(getNotifications(key));
        }
        notifications.sort((n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt()));
          return notifications;
      }

}
