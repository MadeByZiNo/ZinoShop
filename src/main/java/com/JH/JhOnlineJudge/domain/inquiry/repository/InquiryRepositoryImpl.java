package com.JH.JhOnlineJudge.domain.inquiry.repository;

import com.JH.JhOnlineJudge.domain.inquiry.entity.Inquiry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InquiryRepositoryImpl implements InquiryRepository{
    private final InquiryJpaRepository inquiryJpaRepository;

    public Optional<Inquiry> findById(Long id) {return inquiryJpaRepository.findById(id);}

    public Inquiry save(Inquiry inquiry) {
        return inquiryJpaRepository.save(inquiry);
    }

    public Page<Inquiry> findAll(Pageable pageable) {
        return inquiryJpaRepository.findAll(pageable);
    }

    public Page<Inquiry> findAllByUserId(Pageable pageable, Long userId) {
        return inquiryJpaRepository.findAllByUserId(pageable, userId);
    }

}
