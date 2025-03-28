package com.JH.JhOnlineJudge.domain.Image.ProductImage;

import java.util.List;

public interface ProductImageRepository {
    ProductImage save(ProductImage productImage);

    List<ProductImage> saveAll(List<ProductImage> imageList);

    void delete(ProductImage productImage);
}
