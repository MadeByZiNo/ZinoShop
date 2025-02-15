package com.JH.JhOnlineJudge.Image.ReviewImage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReviewImageRepositoryImpl implements ReviewImageRepository {

    private final ReviewImageJpaRepository reviewImageJpaRepository;

    @Override
    public ReviewImage save(ReviewImage reviewImage) {
        return reviewImageJpaRepository.save(reviewImage);
    }


    @Override
    public List<ReviewImage> saveAll(List<ReviewImage> imageList) {
        return reviewImageJpaRepository.saveAll(imageList);
    }


}
