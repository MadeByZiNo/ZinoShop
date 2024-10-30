package com.JH.JhOnlineJudge.product.repository;

import com.JH.JhOnlineJudge.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds")
    Page<Product> findProductsByCategoryIds(@Param("categoryIds")List<Long> categoryIds, Pageable pageable);
}
