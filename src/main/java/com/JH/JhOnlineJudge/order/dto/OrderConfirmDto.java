package com.JH.JhOnlineJudge.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderConfirmDto {
    private String paymentKey;
    private String externalId;
    private int amount;
}
