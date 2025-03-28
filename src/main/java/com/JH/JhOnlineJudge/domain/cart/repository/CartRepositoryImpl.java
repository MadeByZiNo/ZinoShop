package com.JH.JhOnlineJudge.domain.cart.repository;

import com.JH.JhOnlineJudge.domain.cart.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CartRepositoryImpl implements CartRepository{

    private final CartJpaRepository cartJpaRepository;

    @Override
    public Cart save(Cart cart) {
        return cartJpaRepository.save(cart);
    }

}
