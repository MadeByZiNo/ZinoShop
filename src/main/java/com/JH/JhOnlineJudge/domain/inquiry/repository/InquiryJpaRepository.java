package com.JH.JhOnlineJudge.domain.inquiry.repository;

import com.JH.JhOnlineJudge.domain.inquiry.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryJpaRepository extends JpaRepository<Inquiry, Long> {
    Page<Inquiry> findAll(Pageable pageable);
    Page<Inquiry> findAllByUserId(Pageable pageable, Long userId);
}
