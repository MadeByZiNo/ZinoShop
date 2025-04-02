package com.JH.JhOnlineJudge.domain.user.exception;

public class InvalidAuthEmailException extends RuntimeException{
    public InvalidAuthEmailException() {
        super("이메일 인증이 올바르지 않습니다.");
    }
}
