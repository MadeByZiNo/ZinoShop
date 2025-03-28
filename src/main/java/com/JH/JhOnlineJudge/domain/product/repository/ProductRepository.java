package com.JH.JhOnlineJudge.domain.product.repository;

import com.JH.JhOnlineJudge.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    List<Product> findAll();

    void delete(Product product);

    Optional<Product> findById(Long id);

    Page<Product> findProductsByCategoryIds(List<Long> categoryIds, Pageable pageable);

    Page<Product> getProductsPageWithImages(List<Long> categoryIds, Pageable pageable);

    List<Product> findSliceByCategoryIds(List<Long> categoryIds, Pageable pageable);

    Slice<Product> findAllBy(Pageable pageable);

    Slice<Product> findProductsByConditions(List<Long> categoryIds, String name, Pageable pageable);
}
