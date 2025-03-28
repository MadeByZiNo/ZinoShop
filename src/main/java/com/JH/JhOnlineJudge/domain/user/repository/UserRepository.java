package com.JH.JhOnlineJudge.domain.user.repository;

import com.JH.JhOnlineJudge.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByUsername(String username);

    Optional<User> findByOauthId(String oauthId);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

}
