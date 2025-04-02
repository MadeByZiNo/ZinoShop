package com.JH.JhOnlineJudge.domain.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String HOST_EMAIL;

    @Async
    public void send(EmailForm emailForm) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(HOST_EMAIL);
        message.setTo(emailForm.getTo());
        message.setSubject(emailForm.getSubject());
        message.setText(emailForm.getBody());
        mailSender.send(message);
    }
}
