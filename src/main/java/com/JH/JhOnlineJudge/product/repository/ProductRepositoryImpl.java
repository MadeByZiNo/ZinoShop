package com.JH.JhOnlineJudge.product.repository;

import com.JH.JhOnlineJudge.product.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productJpaRepository.delete(product);
    }

    @Override
    public Optional<Product> findById(Long product_id) {
        return productJpaRepository.findById(product_id);
    }

    @Override
    public Page<Product> findProductsByCategoryIds(List<Long> categoryIds, Pageable pageable) {
        return productJpaRepository.findProductsByCategoryIds(categoryIds,pageable);
    }

}
