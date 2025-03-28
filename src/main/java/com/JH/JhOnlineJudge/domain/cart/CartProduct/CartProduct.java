package com.JH.JhOnlineJudge.domain.cart.CartProduct;

import com.JH.JhOnlineJudge.domain.cart.entity.Cart;
import com.JH.JhOnlineJudge.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class CartProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    public CartProduct(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static int sumPricesFromList(List<CartProduct> cartProducts) {
        return cartProducts.stream().mapToInt(cartProduct -> cartProduct.getProduct().getPrice() * cartProduct.getQuantity()).sum();
    }
}
