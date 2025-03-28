package com.JH.JhOnlineJudge.domain.user.exception;

public class LoginFailedException extends RuntimeException{
    public LoginFailedException() {
        super("아이디 혹은 비밀번호가 올바르지 않습니다.");
    }
    public LoginFailedException(String message) {
           super(message);
    }
}
