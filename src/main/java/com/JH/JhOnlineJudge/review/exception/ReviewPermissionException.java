package com.JH.JhOnlineJudge.review.exception;

public class ReviewPermissionException extends RuntimeException{
    public ReviewPermissionException(){
        super("리뷰 작성 권한이 없습니다.");
    }
}
