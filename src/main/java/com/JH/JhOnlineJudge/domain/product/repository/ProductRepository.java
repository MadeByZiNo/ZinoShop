package com.JH.JhOnlineJudge.domain.product.repository;

import com.JH.JhOnlineJudge.domain.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    List<Product> findAll();

    void delete(Product product);

    Optional<Product> findById(Long id);

    Slice<Product> findSliceByCategoryIds(List<Long> categoryIds, Pageable pageable);

    List<Product> findAllById(List<Long> ids);

    Slice<Product> findProductsByConditions(List<Long> categoryIds, String name, Pageable pageable);

    Slice<Product> findSliceByIds(List<Long> ids, Pageable pageable);
}
