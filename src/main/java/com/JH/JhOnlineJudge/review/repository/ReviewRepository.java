package com.JH.JhOnlineJudge.review.repository;

import com.JH.JhOnlineJudge.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewRepository {

    Review save(Review review);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    Page<Review> findByProductId(Long productId, Pageable pageable);
    Page<Review> findByUserId(Long userId, Pageable pageable);
    Optional<Review> findById(Long id);
    void deleteById(Long id);
}
