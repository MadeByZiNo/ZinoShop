package com.JH.JhOnlineJudge.Image.InquiryImage;

import java.util.List;

public interface InquiryImageRepository {
    InquiryImage save(InquiryImage inquiryImage);

    List<InquiryImage> saveAll(List<InquiryImage> imageList);

    List<InquiryImage> findAllByInquiryId(Long inquiryId);
}
