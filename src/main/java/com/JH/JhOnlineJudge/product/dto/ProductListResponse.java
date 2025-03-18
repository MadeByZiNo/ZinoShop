package com.JH.JhOnlineJudge.product.dto;

import com.JH.JhOnlineJudge.Image.ProductImage.ProductImage;
import com.JH.JhOnlineJudge.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class ProductListResponse {
    private Long id;
    private String name;
    private int price;
    private int remain;
    private String url;

    public static ProductListResponse from(Product product) {
        return ProductListResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .remain(product.getRemain())
                .url(product.getImages().stream()
                        .map(ProductImage::getUrl)
                        .findFirst()
                        .orElse(null))
                .build();
    }
}
