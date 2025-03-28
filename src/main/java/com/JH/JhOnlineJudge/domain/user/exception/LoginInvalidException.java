package com.JH.JhOnlineJudge.domain.user.exception;

public class LoginInvalidException extends RuntimeException{
    public LoginInvalidException() {
        super("로그인을 해주세요.");
    }
}
