package com.JH.JhOnlineJudge.product.repository;

import com.JH.JhOnlineJudge.product.domain.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductImageRepositoryImpl implements ProductImageRepository{

    private final ProductImageJpaRepository productImageJpaRepository;

    @Override
    public ProductImage save(ProductImage productImage) {
        return productImageJpaRepository.save(productImage);
    }


    @Override
    public List<ProductImage> saveAll(List<ProductImage> imageList) {
        return productImageJpaRepository.saveAll(imageList);
    }


}
