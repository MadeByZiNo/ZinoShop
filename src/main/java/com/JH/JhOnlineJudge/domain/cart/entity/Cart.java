package com.JH.JhOnlineJudge.domain.cart.entity;

import com.JH.JhOnlineJudge.domain.cart.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY)
    private User user;

    public void attachUser(User user) {
        this.user = user;
    }

    public void clear() {
        cartProducts.clear();
    }
}
