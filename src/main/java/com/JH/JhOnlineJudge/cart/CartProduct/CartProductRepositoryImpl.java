package com.JH.JhOnlineJudge.cart.CartProduct;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartProductRepositoryImpl implements CartProductRepository{

    private final CartProductRepositoryJpa cartProductRepositoryJpa;

    @Override
    public CartProduct save(CartProduct cartProduct) {
        return cartProductRepositoryJpa.save(cartProduct);
    }

    @Override
    public List<CartProduct> findByCartId(Long cartId) {
        return cartProductRepositoryJpa.findByCartId(cartId);
    }

    @Override
    public Optional<CartProduct> findByCartIdAndProductId(Long cartId, Long productId) {
        return cartProductRepositoryJpa.findByCartIdAndProductId(cartId, productId);
    }

    @Override
    public boolean existsByCartIdAndProductId(Long cartId, Long productId) {
        return cartProductRepositoryJpa.existsByCartIdAndProductId(cartId,productId);
    }

    @Override
    public void delete(CartProduct cartProduct) {
        cartProductRepositoryJpa.delete(cartProduct);
    }
}
