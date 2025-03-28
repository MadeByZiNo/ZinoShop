package com.JH.JhOnlineJudge.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderConfirmRequest {
    private String paymentKey;
    private String externalId;
    private int amount;
}
