package com.JH.JhOnlineJudge.common.CartProduct;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartProductRepositoryJpa extends JpaRepository<CartProduct, Long> {
    List<CartProduct> findByCartId(Long cartId);
    boolean existsByCartIdAndProductId(Long cartId, Long productId);
    Optional<CartProduct> findByCartIdAndProductId(Long cartId, Long productId);
    void delete(CartProduct cartProduct);
}
