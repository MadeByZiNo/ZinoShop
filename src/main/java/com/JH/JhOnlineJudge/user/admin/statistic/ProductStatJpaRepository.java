package com.JH.JhOnlineJudge.user.admin.statistic;

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

    @Query("SELECT ps FROM ProductStat ps WHERE ps.product.id = :productId AND FUNCTION('YEAR', ps.date) = :year AND (:month IS NULL OR FUNCTION('MONTH', ps.date) = :month)")
    List<ProductStat> findByProductIdAndYearAndMonth(
            @Param("productId") Long productId,
            @Param("year") Integer year,
            @Param("month") Integer month
    );


    @Query("SELECT ps " +
            "FROM ProductStat ps " +
            "WHERE YEAR(ps.date) = :year " +
            "AND MONTH(ps.date) = :month " +
            "AND DAY(ps.date) = :day")
    List<ProductStat> findDailyStats(@Param("year") Integer year,
                                     @Param("month") Integer month,
                                     @Param("day") Integer day
    );

    @Query("SELECT ps " +
            "FROM ProductStat ps " +
            "WHERE YEAR(ps.date) = :year " +
            "AND MONTH(ps.date) = :month")
    List<ProductStat> findMonthlyStats(@Param("year") Integer year,
                                       @Param("month") Integer month
    );

    @Query("SELECT ps " +
            "FROM ProductStat ps " +
            "WHERE YEAR(ps.date) = :year")
    List<ProductStat> findYearlyStats(@Param("year") Integer year
    );
}

