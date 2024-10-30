package com.JH.JhOnlineJudge.user.repository;

import com.JH.JhOnlineJudge.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

}
