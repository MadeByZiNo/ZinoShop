package com.JH.JhOnlineJudge.domain.inquiry.exception;

public class DuplicateInquiryReplyException extends RuntimeException{
    public DuplicateInquiryReplyException() {
        super("중복된 답변요청입니다.");
    }
}
