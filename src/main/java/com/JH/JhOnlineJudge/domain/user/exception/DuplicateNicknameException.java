package com.JH.JhOnlineJudge.domain.user.exception;

public class DuplicateNicknameException extends RuntimeException{
    public DuplicateNicknameException() {
        super("해당 닉네임이 이미 존재합니다.");
    }
}
