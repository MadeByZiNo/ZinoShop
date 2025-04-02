package com.JH.JhOnlineJudge.domain.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

    @Email(message = "이메일 형식이어야 합니다.")
    @NotEmpty(message = "아이디를 입력해주세요.")
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",message = "영소대문자, 숫자, 특수문자가 하나씩 있어야합니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotEmpty(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirmation;

    @Size(min = 2, max = 10, message = "닉네임는 2자 이상 10자 이하여야 합니다.")
    @NotEmpty(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "배송 주소를 등록해주세요.")
    private String deliveryAddress;

    @NotBlank(message = "상세 주소를 등록해주세요.")
    private String detailAddress;

    public boolean isPasswordConfirmed() {
        return password != null && password.equals(passwordConfirmation);
    }
}
