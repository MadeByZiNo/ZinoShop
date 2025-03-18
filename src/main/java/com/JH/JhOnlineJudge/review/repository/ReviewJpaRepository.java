package com.JH.JhOnlineJudge.review.repository;

import com.JH.JhOnlineJudge.review.domain.Review;
import com.JH.JhOnlineJudge.review.dto.ReviewListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

       boolean existsByUserIdAndProductId(Long userId, Long productId);

       @Query("SELECT new com.JH.JhOnlineJudge.review.dto.ReviewListResponse(" +
               "r.id, u.nickname, r.createdAt, r.content, " +
               "(SELECT i.url FROM ReviewImage i WHERE i.review = r ORDER BY i.id ASC LIMIT 1)) " +
               "FROM Review r " +
               "LEFT JOIN r.user u " +  // ✅
               "WHERE r.product.id = :productId")
       Page<ReviewListResponse> getReviewDTOByProductId(@Param("productId") Long productId, Pageable pageable);


       @Query("SELECT DISTINCT r FROM Review r " +
               "LEFT JOIN FETCH r.user " +
               "LEFT JOIN FETCH r.images " +
               "WHERE r.user.id = :userId")
       Page<Review> findByUserIdWithUserAndImages(@Param("userId") Long userId, Pageable pageable);

}
