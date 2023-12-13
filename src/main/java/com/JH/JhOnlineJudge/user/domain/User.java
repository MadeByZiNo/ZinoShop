package com.JH.JhOnlineJudge.user.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private int id;
    private String username;
    private String password;
    private String nickname;
    private int level_id;

    public User(String username, String password, String nickname, int level_id) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.level_id = level_id;
    }
}
