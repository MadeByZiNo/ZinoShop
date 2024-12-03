package com.JH.JhOnlineJudge.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponDto> getCoupon(@PathVariable Long couponId) {
        CouponDto coupon = couponService.getCouponDtoById(couponId);

        if (coupon == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(coupon);
    }
}
