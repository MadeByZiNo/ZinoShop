package com.JH.JhOnlineJudge.notification.service;

import com.JH.JhOnlineJudge.notification.domain.NotificationFrom;
import com.JH.JhOnlineJudge.notification.dto.NotificationMessageResponse;
import com.JH.JhOnlineJudge.notification.dto.NotificationStatusResponse;
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


    // 알림조회상태를 확인하고 읽지않은 알림 개수 반환
    public NotificationStatusResponse getNotificationStatus(Long userId) {
        String keyScan = keyBase + userId + keyLast;
        List<NotificationMessageResponse> notifications = getAllNotifications(keyScan);
        long unreadCount = notifications.stream()
                        .filter(notification->!notification.isRead())
                        .count();
        return new NotificationStatusResponse(unreadCount > 0, unreadCount);
    }


    // 알림 메시지들 받기
    public List<NotificationMessageResponse> receiveNotificationMessage(Long userId) {
        String keyScan = keyBase + userId + keyLast;
        List<NotificationMessageResponse> notifications = getAllNotifications(keyScan);

      return notifications;
    }


    // 모든 알림 읽음처리
    public void setNotificationRead(Long userId) {
        String keyScan = keyBase + userId + keyLast;
        Set<String> keys = redisTemplate.keys(keyScan);
        keys.stream().forEach(key ->{
            List<NotificationMessageResponse> notifications = getNotifications(key);                 // key로부터 모든 알림 뽑아오기
            redisTemplate.delete(key);                                                          // 현재 key의 데이터를 clear
            notifications.stream().forEach(notification -> {
                notification.setRead(true);                                                  // 뽑았던 알림들 read처리
                redisTemplate.opsForList().rightPush(key,notification);                         // 뽑았던 알림들 재삽입
            });
            redisTemplate.expire(key, Duration.ofDays(7));
        });
    }


    // 알림을 reids에 추가한다(알림을 보낸다)
    public void sendNotificationMessage(Long userId, String message, NotificationFrom from) {
        String key = keyBase + userId + from.getFrom();
        NotificationMessageResponse notificationMessage = new NotificationMessageResponse(message, false, LocalDateTime.now());
        redisTemplate.opsForList().rightPush(key,notificationMessage);
        redisTemplate.expire(key, Duration.ofDays(7));
    }



    // 모든 알림들을 가져와 최신순 정렬 후 반환 (notification.1.*)
    private List<NotificationMessageResponse> getAllNotifications(String keyScan) {
        Set<String> keys = redisTemplate.keys(keyScan);

        List<NotificationMessageResponse> notifications = new ArrayList<>();
        for (String key : keys) {
           notifications.addAll(getNotifications(key));
        }
        notifications.sort((n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt()));
          return notifications;
      }

   // 특정 키의 알림 반환 (notification.1.delivery , notification.1.inquiry 등)
    private List<NotificationMessageResponse> getNotifications(String key) {
        List<NotificationMessageResponse> notifications = new ArrayList<>();

        List<Object> result = redisTemplate.opsForList().range(key, 0, -1);
        notifications = (result.stream()
                .map(notification -> objectMapper.convertValue(notification, NotificationMessageResponse.class))
                .collect(Collectors.toList()));

        return notifications;
    }
}
