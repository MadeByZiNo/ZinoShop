package com.JH.JhOnlineJudge.heart.repository;

import com.JH.JhOnlineJudge.heart.domain.Heart;
import com.JH.JhOnlineJudge.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class HeartRepositoryImpl implements HeartRepository{
    private final HeartJpaRepository heartJpaRepository;

    @Override
    public Heart save(Heart heart) {
        return heartJpaRepository.save(heart);
    }

    @Override
    public void delete(Heart heart) {
        heartJpaRepository.delete(heart);
    }

    @Override
    public Optional<Heart> findByUserIdAndProductId(Long userId, Long productId) {
        return heartJpaRepository.findByUserIdAndProductId(userId, productId);
    }

    @Override
    public boolean existsByUserIdAndProductId(Long userId, Long productId) {
        return heartJpaRepository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    public Page<Heart> findByUser(User user, Pageable pageable) {
        return heartJpaRepository.findByUser(user, pageable);
    }
}
