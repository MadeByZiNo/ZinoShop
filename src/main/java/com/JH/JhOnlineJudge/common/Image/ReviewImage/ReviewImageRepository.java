package com.JH.JhOnlineJudge.common.Image.ReviewImage;

import java.util.List;

public interface ReviewImageRepository {
    ReviewImage save(ReviewImage reviewImage);

    List<ReviewImage> saveAll(List<ReviewImage> imageList);

}
