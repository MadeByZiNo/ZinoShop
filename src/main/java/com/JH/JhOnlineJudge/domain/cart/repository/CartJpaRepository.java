package com.JH.JhOnlineJudge.domain.cart.repository;

import com.JH.JhOnlineJudge.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {
}
