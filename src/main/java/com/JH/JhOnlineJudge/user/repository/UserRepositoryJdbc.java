package com.JH.JhOnlineJudge.user.repository;

import com.JH.JhOnlineJudge.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryJdbc implements UserRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User save(User user) {
        String query = "INSERT INTO user (username, password, nickname,level_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, user.getUsername(),user.getPassword(),user.getNickname(),user.getLevel_id());
        return user;
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        String query = "select * from user where nickname = ?";
        User user = jdbcTemplate.queryForObject(query, RowMapper(),nickname);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String query = "select * from user where username = ?";
        User user = jdbcTemplate.queryForObject(query, RowMapper(),username);
        return Optional.ofNullable(user);
    }

    @Override
    public boolean existsByUsername(String username){
        String query = "SELECT COUNT(*) FROM user WHERE username = ?";
        return jdbcTemplate.queryForObject(query, Integer.class ,username)==0 ? false : true;
    };

    @Override
    public boolean existsByNickname(String nickname){
        String query = "SELECT COUNT(*) FROM user WHERE nickname = ?";
        return jdbcTemplate.queryForObject(query, Integer.class ,nickname)==0 ? false : true;
    };


    private RowMapper<User> RowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setNickname(rs.getString("nickname"));
            user.setLevel_id(rs.getInt("level_id"));
            return user;
        };
    }
}
