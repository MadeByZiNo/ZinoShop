package com.JH.JhOnlineJudge.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponDto getCouponDtoById(Long id) {
        Coupon coupon = couponRepository.findById(id).orElse(null);
        return CouponDto.from(coupon);
    }


    public Coupon findById(Long id) {
        return couponRepository.findById(id).orElse(null);
     }

    public List<CouponDto> findCouponsByUserId(Long userId) {

        List<Coupon> coupons = couponRepository.findAllByUserId(userId);
        return coupons.stream()
                .map(coupon -> CouponDto.from(coupon))
                .collect(Collectors.toList());
    }

    public List<CouponDto> findAvailableCouponsByUserId(Long userId) {

        List<Coupon> coupons = couponRepository.findAllByUserId(userId);
          return coupons.stream()
                .filter(coupon -> !coupon.isUsed())
                .filter(coupon ->  coupon.getExpirationDate().isAfter(LocalDateTime.now()))
                .map(coupon -> CouponDto.from(coupon))
                .collect(Collectors.toList());
      }

      public boolean existsById(Long id) {
        return couponRepository.existsById(id);
      }
}
