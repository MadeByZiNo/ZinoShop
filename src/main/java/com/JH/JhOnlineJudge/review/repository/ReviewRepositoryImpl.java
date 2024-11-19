package com.JH.JhOnlineJudge.review.repository;

import com.JH.JhOnlineJudge.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Review> findByProductId(Long productId, Pageable pageable) {
        return reviewJpaRepository.findByProductId(productId, pageable);
    }

    @Override
    public Page<Review> findByUserId(Long userId, Pageable pageable) {
        return reviewJpaRepository.findByUserId(userId, pageable);
    }
    @Override
    public Optional<Review> findById(Long id){
        return reviewJpaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {reviewJpaRepository.deleteById(id);}
}
