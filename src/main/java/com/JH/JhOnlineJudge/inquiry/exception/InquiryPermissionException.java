package com.JH.JhOnlineJudge.inquiry.exception;

public class InquiryPermissionException extends RuntimeException{
    public InquiryPermissionException(){
        super("문의 확인 권한이 없습니다.");
    }
}
