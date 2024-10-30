package com.JH.JhOnlineJudge.heart;

import com.JH.JhOnlineJudge.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HeartRepository {
    Heart save(Heart heart);
    void delete(Heart heart);
    Optional<Heart> findByUserIdAndProductId(Long userId, Long productId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    Page<Heart> findByUser(User user, Pageable pageable);
}
