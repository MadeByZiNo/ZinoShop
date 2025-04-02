package com.JH.JhOnlineJudge.domain.user.auth;

import com.JH.JhOnlineJudge.domain.email.AuthCodeEmailForm;
import com.JH.JhOnlineJudge.domain.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class EmailAuthService {
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailSender emailSender;

    public void sendAuthCode(String email) {
        String code = generateCode();

        redisTemplate.opsForValue().set("email-auth:" + email, code, Duration.ofMinutes(3));

        emailSender.send(new AuthCodeEmailForm(email, code));
    }

    public boolean verifyCode(String email, String inputCode) {
        String key = "email-auth:" + email;
        String savedCode = redisTemplate.opsForValue().get(key);
        if(inputCode.equals(savedCode)){
            redisTemplate.opsForValue().set("email-auth:verified:" + email, "true", Duration.ofMinutes(10));
            return true;
        }
        return false;
    }


    private String generateCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

}
