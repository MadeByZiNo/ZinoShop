package com.JH.JhOnlineJudge.user.validator;

import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.dto.SignUpDto;
import com.JH.JhOnlineJudge.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class UserValidator implements Validator {
    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        SignUpDto signUpDto = (SignUpDto) target;

        if (!signUpDto.isPasswordConfirmed()) {
            errors.rejectValue("passwordConfirmation", null,"비밀번호와 일치하지 않습니다.");
        }

        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            errors.rejectValue("username", null,"해당 닉네임이 이미 존재합니다.");
        }

        if (userRepository.existsByNickname(signUpDto.getNickname())) {
            errors.rejectValue("nickname", null,"아이디가 이미 존재합니다.");
        }

    }
}
