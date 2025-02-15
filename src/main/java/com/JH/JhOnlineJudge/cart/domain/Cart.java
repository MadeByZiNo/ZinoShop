package com.JH.JhOnlineJudge.cart.domain;

import com.JH.JhOnlineJudge.cart.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.user.domain.User;
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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void attachUser(User user) {
        this.user = user;
    }

    public void clear() {
        cartProducts.clear();
    }
}
