package com.JH.JhOnlineJudge.review.exception;

public class DuplicateReviewException extends RuntimeException{
    public DuplicateReviewException() {
        super("중복된 리뷰요청입니다.");
    }
}
