package com.JH.JhOnlineJudge.cart.repository;

import com.JH.JhOnlineJudge.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<Cart, Long> {
}
