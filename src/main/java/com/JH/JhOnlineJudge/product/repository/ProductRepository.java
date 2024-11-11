package com.JH.JhOnlineJudge.product.repository;

import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.domain.ProductImage;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    void delete(Product product);

    Optional<Product> findById(Long id);

    Optional<Product> findByIdWithLock(Long id);

    Page<Product> findProductsByCategoryIds(List<Long> categoryIds, Pageable pageable);

}
