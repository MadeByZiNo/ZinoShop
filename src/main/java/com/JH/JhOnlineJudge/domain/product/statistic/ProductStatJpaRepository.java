package com.JH.JhOnlineJudge.domain.product.statistic;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ProductStatJpaRepository extends JpaRepository<ProductStat, Long> {

    Page<ProductStat> findAllByDate(LocalDate date, Pageable pageable);

    List<ProductStat> findAllByProductId(Long productId);

    @Query(value = """
    SELECT 
        date AS date,
        SUM(quantity) AS quantity,
        SUM(total_price) AS totalPrice
    FROM product_stat
    WHERE product_id = :productId
      AND YEAR(date) = :year
      AND MONTH(date) = :month
    GROUP BY date
    ORDER BY date
    """, nativeQuery = true)
    List<Object[]> findDailyAggregatedStats(@Param("productId") Long productId,
                                            @Param("year") int year,
                                            @Param("month") int month);


    @Query(value = """
    SELECT 
        YEAR(date) AS year,
        MONTH(date) AS month,
        SUM(quantity) AS quantity,
        SUM(total_price) AS totalPrice
    FROM product_stat
    WHERE product_id = :productId
      AND YEAR(date) = :year
    GROUP BY year, month
    ORDER BY year, month
    """, nativeQuery = true)
    List<Object[]> findMonthlyAggregatedStats(@Param("productId") Long productId, @Param("year") int year);

    @Query(value = """
    SELECT 
        YEAR(date) AS year,
        SUM(quantity) AS quantity,
        SUM(total_price) AS totalPrice
    FROM product_stat
    WHERE product_id = :productId
    GROUP BY year
    ORDER BY year
    """, nativeQuery = true)
    List<Object[]> findYearlyAggregatedStats(@Param("productId") Long productId);


    @Query("""
    SELECT new com.JH.JhOnlineJudge.domain.product.statistic.ProductSalesRankDto(ps.product.name, SUM(ps.quantity), SUM(ps.totalPrice))
    FROM ProductStat ps 
    WHERE ps.date BETWEEN :startDate AND :endDate
    GROUP BY ps.product.id, ps.product.name
    ORDER BY SUM(ps.totalPrice) DESC
    """)
    List<ProductSalesRankDto> getTop30ByPrice(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    @Query("""
    SELECT new com.JH.JhOnlineJudge.domain.product.statistic.ProductSalesRankDto(ps.product.name, SUM(ps.quantity), SUM(ps.totalPrice))
    FROM ProductStat ps
    WHERE ps.date BETWEEN :startDate AND :endDate
    GROUP BY ps.product.id, ps.product.name
    ORDER BY SUM(ps.quantity) DESC
    """)
    List<ProductSalesRankDto> getTop30ByQuantity(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

}

