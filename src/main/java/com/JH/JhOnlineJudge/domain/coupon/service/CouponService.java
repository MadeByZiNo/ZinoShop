package com.JH.JhOnlineJudge.domain.coupon.service;

import com.JH.JhOnlineJudge.domain.coupon.dto.CouponResponse;
import com.JH.JhOnlineJudge.domain.coupon.entity.Coupon;
import com.JH.JhOnlineJudge.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;


    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id).orElse(null);
     }

    public List<CouponResponse> getAvailableCouponsByUserId(Long userId) {

        List<Coupon> coupons = couponRepository.findAllByUserId(userId);
          return coupons.stream()
                .filter(coupon -> !coupon.isUsed())
                .filter(coupon ->  coupon.getExpirationDate().isAfter(LocalDateTime.now()))
                .map(coupon -> CouponResponse.from(coupon))
                .collect(Collectors.toList());
      }

}
