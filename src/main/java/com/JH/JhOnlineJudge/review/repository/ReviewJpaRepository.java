package com.JH.JhOnlineJudge.review.repository;

import com.JH.JhOnlineJudge.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

       boolean existsByUserIdAndProductId(Long userId, Long productId);
       Page<Review> findByProductId(Long productId, Pageable pageable);
       Page<Review> findByUserId(Long userId, Pageable pageable);
}
