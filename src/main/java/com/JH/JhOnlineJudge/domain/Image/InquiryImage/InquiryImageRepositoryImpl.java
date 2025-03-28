package com.JH.JhOnlineJudge.domain.Image.InquiryImage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class InquiryImageRepositoryImpl implements InquiryImageRepository {

    private final InquiryImageJpaRepository inquiryImageJpaRepository;

    @Override
    public List<InquiryImage> findAllByInquiryId(Long inquiryId){
        return inquiryImageJpaRepository.findAllByInquiryId(inquiryId);
    }
    @Override
    public InquiryImage save(InquiryImage inquiryImage) {
        return inquiryImageJpaRepository.save(inquiryImage);
    }


    @Override
    public List<InquiryImage> saveAll(List<InquiryImage> imageList) {
        return inquiryImageJpaRepository.saveAll(imageList);
    }


}
