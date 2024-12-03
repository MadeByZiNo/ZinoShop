package com.JH.JhOnlineJudge.coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findAllByUserId(Long userId);
}
