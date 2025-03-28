package com.JH.JhOnlineJudge.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderProductDetailDto {
    private String name;
    private int quantity;
    private int price;
}
