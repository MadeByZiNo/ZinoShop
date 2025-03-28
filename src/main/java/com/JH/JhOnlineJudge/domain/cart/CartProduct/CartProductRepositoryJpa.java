package com.JH.JhOnlineJudge.domain.cart.CartProduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartProductRepositoryJpa extends JpaRepository<CartProduct, Long> {
    List<CartProduct> findByCartId(Long cartId);
    boolean existsByCartIdAndProductId(Long cartId, Long productId);
    Optional<CartProduct> findByCartIdAndProductId(Long cartId, Long productId);
    void delete(CartProduct cartProduct);

    @Query("SELECT cp FROM CartProduct cp " +
            "JOIN FETCH cp.product " +
            "WHERE cp.cart.id = (SELECT u.cart.id FROM User u WHERE u.id = :userId)")
    List<CartProduct> findCartProductsWithProduct(@Param("userId") Long userId);


}
