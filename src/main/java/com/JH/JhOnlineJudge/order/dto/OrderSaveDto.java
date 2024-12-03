package com.JH.JhOnlineJudge.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSaveDto {
    private String recipientName;
    private String deliveryAddress;
    private String detailAddress;
    private int discountedPrice;
    private String discountInfo;
    private int couponDiscountPrice;
    private int rewardPointsDiscountPrice;
    private String memo;
    private Long couponId;
}