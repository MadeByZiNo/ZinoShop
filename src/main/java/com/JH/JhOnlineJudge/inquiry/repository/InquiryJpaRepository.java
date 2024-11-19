package com.JH.JhOnlineJudge.inquiry.repository;

import com.JH.JhOnlineJudge.inquiry.domain.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryJpaRepository extends JpaRepository<Inquiry, Long> {
    Page<Inquiry> findAll(Pageable pageable);
    Page<Inquiry> findAllByUserId(Pageable pageable, Long userId);
}
