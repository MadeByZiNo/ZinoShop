package com.JH.JhOnlineJudge.user.validator;

import com.JH.JhOnlineJudge.user.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class SignupValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpRequest signUpDto = (SignUpRequest) target;

        if (!signUpDto.isPasswordConfirmed()) {
            errors.rejectValue("passwordConfirmation", null,"비밀번호와 일치하지 않습니다.");
        }

    }
}
