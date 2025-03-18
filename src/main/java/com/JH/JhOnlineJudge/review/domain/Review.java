package com.JH.JhOnlineJudge.review.domain;

import com.JH.JhOnlineJudge.Image.ReviewImage.ReviewImage;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.user.domain.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @BatchSize(size = 10)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private String content;

    @OneToMany(mappedBy = "review" , cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    @BatchSize(size = 10)
    private List<ReviewImage> images = new ArrayList<>();

    @Builder
    private Review(User user, Product product, LocalDateTime createdAt, String content, List<ReviewImage> images) {
        this.user = user;
        this.product = product;
        this.createdAt = createdAt;
        this.content = content;
    }

    public static Review of(User user, Product product, String content) {
        return Review.builder()
                .user(user)
                .product(product)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
