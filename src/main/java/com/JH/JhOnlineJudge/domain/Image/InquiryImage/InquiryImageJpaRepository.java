package com.JH.JhOnlineJudge.domain.Image.InquiryImage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryImageJpaRepository extends JpaRepository<InquiryImage, Long> {

    List<InquiryImage> findAllByInquiryId(Long inquiryId);
}
