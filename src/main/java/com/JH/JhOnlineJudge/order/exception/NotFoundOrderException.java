package com.JH.JhOnlineJudge.order.exception;

public class NotFoundOrderException extends RuntimeException{
    public NotFoundOrderException() {
           super("주문 정보가 존재하지 않습니다.");
       }
}
