package com.JH.JhOnlineJudge.domain.cart.dto;

import lombok.Getter;

@Getter
public class  UpdateQuantityRequest {
    private Long productId;
    private int quantity;
}
