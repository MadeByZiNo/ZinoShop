package com.JH.JhOnlineJudge.domain.Image.ProductImage;

import com.JH.JhOnlineJudge.domain.Image.Image;
import com.JH.JhOnlineJudge.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class ProductImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder
    public ProductImage(String url, Product product) {
        super(url);
        attachProduct(product);
    }

    public void attachProduct(Product product) {
        this.product = product;
        product.getImages().add(this);
    }
}
