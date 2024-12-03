package com.JH.JhOnlineJudge.common.statistic;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductStatJpaRepository extends JpaRepository<ProductStat, Long> {
    List<ProductStat> findByDate(LocalDate date);
}

