package com.JH.JhOnlineJudge.common.statistic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ProductStatJpaRepository extends JpaRepository<ProductStat, Long> {

    Page<ProductStat> findAllByDate( LocalDate date, Pageable pageable);

}

