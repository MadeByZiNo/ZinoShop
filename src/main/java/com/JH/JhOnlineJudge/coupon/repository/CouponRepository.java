package com.JH.JhOnlineJudge.coupon.repository;

import com.JH.JhOnlineJudge.coupon.domain.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    List<Coupon> findAllByUserId(Long userId);
    Optional<Coupon> findById(Long id);
    boolean existsById(Long id);

}
