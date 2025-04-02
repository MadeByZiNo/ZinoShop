package com.JH.JhOnlineJudge.domain.email;

public class WelcomeEmailForm implements EmailForm {

    private final String to;
    private final String nickname;

    public WelcomeEmailForm(String to, String nickname) {
        this.to = to;
        this.nickname = nickname;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public String getSubject() {
        return "🎉 회원가입을 환영합니다!";
    }

    @Override
    public String getBody() {
        return nickname + "님, Zinoshop에 오신 걸 환영해요!";
    }
}