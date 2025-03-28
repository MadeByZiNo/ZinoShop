package com.JH.JhOnlineJudge.domain.product.dto;

import com.JH.JhOnlineJudge.domain.product.entity.Product;
import com.JH.JhOnlineJudge.domain.product.entity.ProductState;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductDto {
    @Nullable
    private Long id;
    private String name;
    private int price;
    private String description;
    private int remain;
    private ProductState state;
    private Long categoryId;

    public static ProductDto from(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getRemain(),
                product.getState(),
                product.getCategory().getId()
        );
    }
}
