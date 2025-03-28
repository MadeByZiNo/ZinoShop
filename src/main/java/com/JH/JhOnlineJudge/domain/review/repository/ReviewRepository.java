package com.JH.JhOnlineJudge.domain.review.repository;

import com.JH.JhOnlineJudge.domain.review.entity.Review;
import com.JH.JhOnlineJudge.domain.review.dto.ReviewListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository {

    Review save(Review review);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    Optional<Review> findById(Long id);
    void deleteById(Long id);
    Page<Review> getByUserIdWithUserAndImages(Long userId, Pageable pageable);
    Page<ReviewListResponse> getReviewDTOByProductId(@Param("productId") Long productId, Pageable pageable);

}
