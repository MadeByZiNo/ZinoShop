package com.JH.JhOnlineJudge.domain.user.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException() {
        super("접근권한이 없습니다.");
    }
}
