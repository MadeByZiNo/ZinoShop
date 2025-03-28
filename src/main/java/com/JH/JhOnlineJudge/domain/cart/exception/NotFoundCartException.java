package com.JH.JhOnlineJudge.domain.cart.exception;

public class NotFoundCartException extends RuntimeException{
    public NotFoundCartException() {
           super("카트가 존재하지 않습니다.");
       }
}
