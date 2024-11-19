package com.JH.JhOnlineJudge.review.exception;

public class NotFoundReviewException extends RuntimeException{
    public NotFoundReviewException() {super("리뷰를 찾을 수 없습니다.");
    }
}
