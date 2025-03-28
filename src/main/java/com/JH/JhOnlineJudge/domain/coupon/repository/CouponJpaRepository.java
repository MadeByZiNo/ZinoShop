package com.JH.JhOnlineJudge.domain.coupon.repository;

import com.JH.JhOnlineJudge.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findAllByUserId(Long userId);
}
