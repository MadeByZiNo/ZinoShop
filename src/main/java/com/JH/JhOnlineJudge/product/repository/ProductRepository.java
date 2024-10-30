package com.JH.JhOnlineJudge.product.repository;

import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.domain.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    void delete(Product product);

    Optional<Product> findById(Long product_id);

    Page<Product> findProductsByCategoryIds(List<Long> categoryIds, Pageable pageable);

}
