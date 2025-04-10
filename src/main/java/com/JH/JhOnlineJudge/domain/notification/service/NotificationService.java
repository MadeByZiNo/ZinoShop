package com.JH.JhOnlineJudge.domain.notification.service;

import com.JH.JhOnlineJudge.common.utils.RedisHelper;
import com.JH.JhOnlineJudge.domain.notification.entity.NotificationFrom;
import com.JH.JhOnlineJudge.domain.notification.dto.NotificationMessageResponse;
import com.JH.JhOnlineJudge.domain.notification.dto.NotificationStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RedisHelper redisHelper;


    private final String keyBase = "notification.";
    private final String keyLast = ".*";


    // 알림조회상태를 확인하고 읽지않은 알림 개수 반환
    public NotificationStatusResponse getNotificationStatus(Long userId) {
        String keyScan = keyBase + userId + keyLast;
        List<NotificationMessageResponse> notifications = getAllNotifications(keyScan);
        long unreadCount = notifications.stream()
                .filter(notification -> !notification.isRead())
                .count();
        return new NotificationStatusResponse(unreadCount > 0, unreadCount);
    }

    // 알림 메시지들 받기
    public List<NotificationMessageResponse> receiveNotificationMessage(Long userId) {
        String keyScan = keyBase + userId + keyLast;
        return getAllNotifications(keyScan);
    }

    // 모든 알림 읽음처리
    public void setNotificationRead(Long userId) {
        String keyScan = keyBase + userId + keyLast;
        Set<String> keys = redisHelper.getKeys(keyScan);

        keys.forEach(key -> {
            List<NotificationMessageResponse> notifications = redisHelper.getListData(key, NotificationMessageResponse.class);
            redisHelper.deleteData(key);

            notifications.forEach(notification -> notification.setRead(true));

            redisHelper.saveListData(key, notifications, Duration.ofDays(7));
        });
    }

    // 알림을 redis에 추가한다(알림을 보낸다)
    public void sendNotificationMessage(Long userId, String message, NotificationFrom from) {
        String key = keyBase + userId + from.getFrom();
        NotificationMessageResponse notificationMessage = new NotificationMessageResponse(message, false, LocalDateTime.now());

        List<NotificationMessageResponse> existing = redisHelper.getListData(key, NotificationMessageResponse.class);
        existing.add(notificationMessage);

        redisHelper.saveListData(key, existing, Duration.ofDays(7));
    }

    // 모든 알림들을 가져와 최신순 정렬 후 반환
    private List<NotificationMessageResponse> getAllNotifications(String keyScan) {
        Set<String> keys = redisHelper.getKeys(keyScan);
        List<NotificationMessageResponse> notifications = new ArrayList<>();

        for (String key : keys) {
            notifications.addAll(redisHelper.getListData(key, NotificationMessageResponse.class));
        }

        notifications.sort((n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt()));
        return notifications;
    }
}
