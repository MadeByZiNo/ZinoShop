package com.JH.JhOnlineJudge.inquiry.exception;

public class DuplicateInquiryReplyException extends RuntimeException{
    public DuplicateInquiryReplyException() {
        super("중복된 답변요청입니다.");
    }
}
