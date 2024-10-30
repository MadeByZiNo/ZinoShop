package com.JH.JhOnlineJudge.user.exception;

public class DuplicateUsernameException extends RuntimeException{
    public DuplicateUsernameException() {
        super("해당 아이디가 이미 존재합니다.");
    }
}
