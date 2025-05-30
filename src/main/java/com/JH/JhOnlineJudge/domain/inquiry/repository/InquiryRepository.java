package com.JH.JhOnlineJudge.domain.inquiry.repository;

import com.JH.JhOnlineJudge.domain.inquiry.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InquiryRepository {
    Optional<Inquiry> findById(Long id);
    Inquiry save(Inquiry inquiry);
    Page<Inquiry> findAll(Pageable pageable);
    Page<Inquiry> findAllByUserId(Pageable pageable, Long userId);

}
