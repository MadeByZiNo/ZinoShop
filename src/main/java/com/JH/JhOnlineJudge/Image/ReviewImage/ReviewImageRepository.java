package com.JH.JhOnlineJudge.Image.ReviewImage;

import java.util.List;

public interface ReviewImageRepository {
    ReviewImage save(ReviewImage reviewImage);

    List<ReviewImage> saveAll(List<ReviewImage> imageList);

}
