package com.JH.JhOnlineJudge.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class NotificationMessageDto {

    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;

}
