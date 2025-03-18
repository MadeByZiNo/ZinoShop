package com.JH.JhOnlineJudge.order.exception;

public class InvalidOrderException extends RuntimeException{
    public InvalidOrderException() {
           super("주문 정보가 존재하지 않습니다.");
       }
    public InvalidOrderException(String msg) {
             super(msg);
         }
}
