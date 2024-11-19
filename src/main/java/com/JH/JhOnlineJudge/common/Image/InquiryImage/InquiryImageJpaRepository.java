package com.JH.JhOnlineJudge.common.Image.InquiryImage;

import com.JH.JhOnlineJudge.inquiry.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryImageJpaRepository extends JpaRepository<InquiryImage, Long> {

    List<InquiryImage> findAllByInquiryId(Long inquiryId);
}
