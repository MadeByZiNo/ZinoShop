package com.JH.JhOnlineJudge.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsLoginRequest {
    private Boolean isLogin;
    private Long userId;
}
