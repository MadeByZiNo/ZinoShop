package com.JH.JhOnlineJudge.product.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class ProductImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    @Builder
    public ProductImage(String url,  Product product) {
        this.url = url;
        attachProduct(product);
    }

    public void attachProduct(Product product) {
           this.product = product;
           product.getImages().add(this);
       }
}
