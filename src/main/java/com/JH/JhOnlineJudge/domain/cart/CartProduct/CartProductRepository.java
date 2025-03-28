package com.JH.JhOnlineJudge.domain.cart.CartProduct;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository {

    CartProduct save(CartProduct cartProduct);
    List<CartProduct> findByCartId(Long cartId);
    boolean existsByCartIdAndProductId(Long cartId, Long productId);
    Optional<CartProduct> findByCartIdAndProductId(Long cartId, Long productId);
    void delete(CartProduct cartProduct);
    List<CartProduct> findCartProductsWithProduct(Long userId);

}
