package com.JH.JhOnlineJudge.review.repository;

import com.JH.JhOnlineJudge.review.domain.Review;
import com.JH.JhOnlineJudge.review.dto.ReviewListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository{
    private final ReviewJpaRepository reviewJpaRepository;

    @Override
    public Review save(Review review) {
        return reviewJpaRepository.save(review);
    }

    @Override
    public boolean existsByUserIdAndProductId(Long userId, Long productId) {
        return reviewJpaRepository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public Page<ReviewListResponse> getReviewDTOByProductId(Long productId, Pageable pageable){
        return reviewJpaRepository.getReviewDTOByProductId(productId, pageable);
    }

    @Override
    public Page<Review> getByUserIdWithUserAndImages(Long userId, Pageable pageable){
        return reviewJpaRepository.findByUserIdWithUserAndImages(userId, pageable);
    }

    @Override
    public Optional<Review> findById(Long id){
        return reviewJpaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {reviewJpaRepository.deleteById(id);}


}
