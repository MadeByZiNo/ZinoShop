package com.JH.JhOnlineJudge.Image;

public class S3FileUploadException  extends RuntimeException{
    public S3FileUploadException(String e) {
        super("파일 업로드 오류 :  " + e);
    }
}
