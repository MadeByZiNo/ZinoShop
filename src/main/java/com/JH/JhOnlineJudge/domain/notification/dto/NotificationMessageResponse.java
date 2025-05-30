package com.JH.JhOnlineJudge.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class NotificationMessageResponse {

    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;

}
