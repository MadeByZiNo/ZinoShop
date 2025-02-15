package com.JH.JhOnlineJudge.notification.controller;

import com.JH.JhOnlineJudge.notification.service.NotificationService;
import com.JH.JhOnlineJudge.notification.dto.NotificationMessageResponse;
import com.JH.JhOnlineJudge.notification.dto.NotificationStatusResponse;
import com.JH.JhOnlineJudge.user.domain.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/status")
    public ResponseEntity<NotificationStatusResponse> getNotificationStatus(@AuthUser Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationStatus(userId));
    }

    @GetMapping
    public ResponseEntity<List<NotificationMessageResponse>> getNotifications(@AuthUser Long userId) {
        return ResponseEntity.ok(notificationService.receiveNotificationMessage(userId));
    }

    @PostMapping("/read")
    public void setNotificationRead(@AuthUser Long userId) {
        notificationService.setNotificationRead(userId);
    }

}
