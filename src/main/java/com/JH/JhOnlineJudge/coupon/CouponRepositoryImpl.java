package com.JH.JhOnlineJudge.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository{

    private final CouponJpaRepository couponJpaRepository;

    @Override
    public List<Coupon> findAllByUserId(Long userId) {
        return couponJpaRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<Coupon> findById(Long id) {
        return couponJpaRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id){return couponJpaRepository.existsById(id);}
}
