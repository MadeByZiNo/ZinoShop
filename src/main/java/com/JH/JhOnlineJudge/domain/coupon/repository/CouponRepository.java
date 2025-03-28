package com.JH.JhOnlineJudge.domain.coupon.repository;

import com.JH.JhOnlineJudge.domain.coupon.entity.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponRepository {
    List<Coupon> findAllByUserId(Long userId);
    Optional<Coupon> findById(Long id);
    boolean existsById(Long id);

}
