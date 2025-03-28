package com.JH.JhOnlineJudge.domain.product.entity;

import com.JH.JhOnlineJudge.domain.category.entity.Category;
import com.JH.JhOnlineJudge.domain.cart.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.domain.Image.ProductImage.ProductImage;
import com.JH.JhOnlineJudge.domain.order.OrderProduct.OrderProduct;
import com.JH.JhOnlineJudge.domain.heart.entity.Heart;
import com.JH.JhOnlineJudge.domain.product.dto.ProductDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

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
    @BatchSize(size = 30)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "product" , orphanRemoval = true)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Version
    private Long version;

    @Builder
    public Product(Long id, String name, int price, String description, int remain, ProductState state) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.remain = remain;
        this.state = state;
        if(remain < 1) {
            this.state = ProductState.품절;
        }
    }

    public static Product of(ProductDto request) {
        return Product.builder()
                      .name(request.getName())
                      .price(request.getPrice())
                      .description(request.getDescription())
                      .remain(request.getRemain())
                      .state(request.getState())
                      .build();
    }

    public void updateRemain(int value) {
        this.remain += value;
        if(remain <= 0) {
            this.state = ProductState.품절;
        }
    }

    public void updateProduct(ProductDto productDto) {
        this.name = productDto.getName();
        this.price = productDto.getPrice();
        this.description = productDto.getDescription();
        this.remain = productDto.getRemain();
        this.state = productDto.getState();
    }

    public void attachCategory(Category category) {
       this.category = category;
       category.getProducts().add(this);
    }

    public void removeImage(ProductImage image) {
        this.images.remove(image);
    }
}
