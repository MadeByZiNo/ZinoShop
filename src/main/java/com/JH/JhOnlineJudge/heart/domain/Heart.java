package com.JH.JhOnlineJudge.heart.domain;

import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder
    public Heart(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public static Heart of(User user, Product product) {
        return new Heart(user, product);
    }
}
