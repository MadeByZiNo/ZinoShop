package com.JH.JhOnlineJudge.cart.dto;

import lombok.Getter;

@Getter
public class    UpdateQuantityRequest {
    private Long productId;
    private int quantity;
}
