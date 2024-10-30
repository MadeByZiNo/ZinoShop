package com.JH.JhOnlineJudge.product.domain;

import com.JH.JhOnlineJudge.category.CategoryService;
import com.JH.JhOnlineJudge.category.domain.Category;
import com.JH.JhOnlineJudge.common.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.heart.Heart;
import com.JH.JhOnlineJudge.product.dto.ProductCreateDto;
import com.JH.JhOnlineJudge.user.domain.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String thumbnail;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int remain;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "product" , orphanRemoval = true)
    private List<CartProduct> cartProducts = new ArrayList<>();


    @Builder
    public Product(Long id, String name, int price, String thumbnail, String description, int remain, Category category, ProductState state) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.thumbnail = thumbnail;
        this.description = description;
        this.remain = remain;
        this.state = state;
        if(remain < 1) {
            this.state = ProductState.품절;
        }

        attachCategory(category);
    }

    public static Product of(ProductCreateDto request, Category category) {
        return Product.builder()
                      .name(request.getName())
                      .price(request.getPrice())
                      .description(request.getDescription())
                      .thumbnail(request.getThumbnail())
                      .remain(request.getRemain())
                      .category(category)
                      .state(request.getState())
                      .build();
    }

    public void updateRemain(int value) {
        this.remain += value;
    }

    public void attachCategory(Category category) {
           this.category = category;
           category.getProducts().add(this);
       }
}
