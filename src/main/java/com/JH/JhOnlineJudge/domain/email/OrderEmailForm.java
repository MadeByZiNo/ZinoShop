package com.JH.JhOnlineJudge.domain.email;

import org.joda.time.DateTime;

public class OrderEmailForm implements EmailForm {

    private final String to;
    private final String nickname;
    private final String orderId;
    private final String orderTime;
    public OrderEmailForm(String to, String nickname, String orderId, String orderTime) {
        this.to = to;
        this.nickname = nickname;
        this.orderId = orderId;
        this.orderTime = orderTime;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public String getSubject() {
        return nickname + "님의 주문이 완료되었습니다.";
    }

    @Override
    public String getBody() {
        return "주문번호 : " + orderId +"\n 주문시간 : " + orderTime;
    }
}
