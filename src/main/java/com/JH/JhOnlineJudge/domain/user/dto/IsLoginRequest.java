package com.JH.JhOnlineJudge.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsLoginRequest {
    private Boolean isLogin;
    private Long userId;
}
