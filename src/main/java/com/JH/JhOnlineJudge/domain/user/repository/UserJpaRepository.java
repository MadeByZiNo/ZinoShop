package com.JH.JhOnlineJudge.domain.user.repository;

import com.JH.JhOnlineJudge.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
    Optional<User> findByUsername(String username);
    Optional<User> findByOauthId(String oauthId);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);

    @Query(value = """
    SELECT u.*
    FROM user u
    JOIN (
        SELECT id
        FROM user
        WHERE id > :lastId
        ORDER BY id
        LIMIT :limit
    ) temp ON u.id = temp.id
    ORDER BY u.id
    """, nativeQuery = true)
    List<User> findNextChunkByLastIdNative(
            @Param("lastId") Long lastId,
            @Param("limit") int limit
    );


}
