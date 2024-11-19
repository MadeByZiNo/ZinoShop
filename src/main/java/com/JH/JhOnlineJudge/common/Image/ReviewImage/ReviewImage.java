package com.JH.JhOnlineJudge.common.Image.ReviewImage;

import com.JH.JhOnlineJudge.common.Image.Image;
import com.JH.JhOnlineJudge.review.domain.Review;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class ReviewImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Builder
    public ReviewImage(String url, Review review) {
        super(url);
        attachReview(review);
    }

    public void attachReview(Review review) {
        this.review = review;
        review.getImages().add(this);
    }
}