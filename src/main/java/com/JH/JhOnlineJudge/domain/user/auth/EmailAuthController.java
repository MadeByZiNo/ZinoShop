package com.JH.JhOnlineJudge.domain.user.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmailAuthController {
    private final EmailAuthService emailAuthService;

    @GetMapping("/email/send-code")
    public ResponseEntity<?> sendCode(@RequestParam String email) {
        emailAuthService.sendAuthCode(email);
        return ResponseEntity.ok().body(Map.of("success", true));
    }

    @PostMapping("/email/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody EmailCodeRequest request) {
        boolean verified = emailAuthService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok().body(Map.of("success", verified));
    }
}
