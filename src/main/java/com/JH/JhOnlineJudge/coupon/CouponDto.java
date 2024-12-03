package com.JH.JhOnlineJudge.coupon;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CouponDto {
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


    public static CouponDto from(Coupon coupon) {
          return new CouponDto(
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
