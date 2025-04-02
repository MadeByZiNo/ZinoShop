package com.JH.JhOnlineJudge.common.notification.entity;

public enum NotificationFrom {
    배송(".delivery"),
    문의(".inquiry");

    private final String from;

    private NotificationFrom(String from) {
         this.from = from;
     }

    public String getFrom(){
        return from;
    }

}
