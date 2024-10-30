package com.JH.JhOnlineJudge.product.repository;

import com.JH.JhOnlineJudge.product.domain.ProductImage;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductImageRepository {
    public ProductImage save(ProductImage productImage);

    public List<ProductImage> saveAll(List<ProductImage> imageList);

}
