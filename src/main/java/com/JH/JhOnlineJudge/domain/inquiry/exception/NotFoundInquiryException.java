package com.JH.JhOnlineJudge.domain.inquiry.exception;

public class NotFoundInquiryException extends RuntimeException{
    public NotFoundInquiryException() {super("문의을 찾을 수 없습니다.");
    }
}
