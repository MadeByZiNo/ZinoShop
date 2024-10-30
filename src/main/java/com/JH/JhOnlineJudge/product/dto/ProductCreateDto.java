package com.JH.JhOnlineJudge.product.dto;

import com.JH.JhOnlineJudge.product.domain.ProductState;
import lombok.Getter;

@Getter
public class ProductCreateDto {
      private String name;

      private int price;

      private String thumbnail;

      private String description;

      private int remain;

      private ProductState state;

      private Long categoryId;
}
