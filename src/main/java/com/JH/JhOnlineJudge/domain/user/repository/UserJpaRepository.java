package com.JH.JhOnlineJudge.domain.user.repository;

import com.JH.JhOnlineJudge.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
    Optional<User> findByUsername(String username);
    Optional<User> findByOauthId(String oauthId);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);

}
