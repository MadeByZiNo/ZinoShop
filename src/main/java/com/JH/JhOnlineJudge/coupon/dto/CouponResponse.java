package com.JH.JhOnlineJudge.coupon.dto;

import com.JH.JhOnlineJudge.coupon.domain.Coupon;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CouponResponse {
    private Long id;
    private String name;
    private String type;
    private LocalDateTime expirationDate;
    private boolean used;
    private int minAmount;

    @Nullable
    private Double discountRate;
    @Nullable
    private Integer discountAmount;


    public static CouponResponse from(Coupon coupon) {
          return new CouponResponse(
              coupon.getId(),
              coupon.getName(),
              coupon.getType().name(),
              coupon.getExpirationDate(),
              coupon.isUsed(),
              coupon.getMinAmount(),
              coupon.getDiscountRate(),
              coupon.getDiscountAmount()
          );
      }
}
