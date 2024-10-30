package com.JH.JhOnlineJudge.cart.repository;

import com.JH.JhOnlineJudge.cart.domain.Cart;
import com.JH.JhOnlineJudge.common.CartProduct.CartProduct;

import java.util.Optional;

public interface CartRepository {
    Cart save(Cart cart);

}
