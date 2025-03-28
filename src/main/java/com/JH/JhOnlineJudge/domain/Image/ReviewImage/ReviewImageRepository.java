package com.JH.JhOnlineJudge.domain.Image.ReviewImage;

import java.util.List;

public interface ReviewImageRepository {
    ReviewImage save(ReviewImage reviewImage);

    List<ReviewImage> saveAll(List<ReviewImage> imageList);

}
