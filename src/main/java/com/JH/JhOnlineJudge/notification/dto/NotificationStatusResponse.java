package com.JH.JhOnlineJudge.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationStatusResponse {

    private boolean exists;
    private Long count;

}