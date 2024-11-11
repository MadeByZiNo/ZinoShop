package com.JH.JhOnlineJudge.order.exception;

public class InsufficientRemainException extends RuntimeException{
    public InsufficientRemainException() {
           super("주문 정보가 존재하지 않습니다.");
       }
    public InsufficientRemainException(String msg) {
             super(msg);
         }
}
