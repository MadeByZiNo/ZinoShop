package com.JH.JhOnlineJudge.domain.user.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailCodeRequest {
    private String email;
    private String code;
}
