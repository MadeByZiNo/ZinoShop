package com.JH.JhOnlineJudge.domain.email;

public class AuthCodeEmailForm implements EmailForm {

    private final String to;
    private final String code;

    public AuthCodeEmailForm(String to, String code) {
        this.to = to;
        this.code = code;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public String getSubject() {
        return "[ZinoShop 가입 인증 코드]";
    }

    @Override
    public String getBody() {
        return "인증 코드: " + code;
    }
}
