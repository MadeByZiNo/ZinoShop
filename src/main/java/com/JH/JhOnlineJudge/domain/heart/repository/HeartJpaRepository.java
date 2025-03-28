package com.JH.JhOnlineJudge.domain.heart.repository;

import com.JH.JhOnlineJudge.domain.heart.entity.Heart;
import com.JH.JhOnlineJudge.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartJpaRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByUserIdAndProductId(Long userId, Long productId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    Page<Heart> findByUser(User user, Pageable pageable);
}
