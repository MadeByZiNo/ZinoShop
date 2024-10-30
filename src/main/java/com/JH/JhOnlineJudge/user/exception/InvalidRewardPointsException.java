package com.JH.JhOnlineJudge.user.exception;

public class InvalidRewardPointsException extends RuntimeException{
    public InvalidRewardPointsException() {
           super("포인트 값이 올바르지 않습니다.");
       }
}
