package com.JH.JhOnlineJudge.user.exception;

public class NotFoundUserException extends RuntimeException{
    public NotFoundUserException() {
           super("유저가 존재하지 않습니다.");
       }
}
