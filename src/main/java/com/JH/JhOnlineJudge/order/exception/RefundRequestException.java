package com.JH.JhOnlineJudge.order.exception;

public class RefundRequestException extends RuntimeException{
    public RefundRequestException(String e) {
        super("toss 환불 요청 에러 발생 :  " + e);
    }
}
