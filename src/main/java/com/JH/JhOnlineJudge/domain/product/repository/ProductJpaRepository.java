package com.JH.JhOnlineJudge.domain.product.repository;

import com.JH.JhOnlineJudge.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds")
    Page<Product> findProductsByCategoryIds(@Param("categoryIds")List<Long> categoryIds, Pageable pageable);

    @Query(value = "SELECT p FROM Product p " +
            "WHERE p.category.id IN :categoryIds",
            countQuery = "SELECT COUNT(p) FROM Product p WHERE p.category.id IN :categoryIds")
    Page<Product> getProductsPageWithImages(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds ORDER BY p.id ASC")
    Slice<Product> findSliceByCategoryIds(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    Slice<Product> findAllBy(Pageable pageable);


    @Query("SELECT p FROM Product p " +
            "WHERE (:name IS NULL OR p.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:categoryIds IS NULL OR p.category.id IN :categoryIds) " +
            "ORDER BY p.id ASC")
    Slice<Product> findProductsByConditions(
            @Param("categoryIds")  List<Long> categoryIds,
            @Param("name") String name,
            Pageable pageable
    );


    @Query("SELECT p FROM Product p " +
    "WHERE p.category.id IN :categoryIds " +
    "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
    "ORDER BY p.id ASC")
    Slice<Product> findSliceByCategoryAndKeyword(
            @Param("categoryIds") List<Long> categoryIds,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"category"})
    Slice<Product> findSliceBy(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    Slice<Product> findSliceByIds(@Param("ids") List<Long> ids, Pageable pageable);
}
