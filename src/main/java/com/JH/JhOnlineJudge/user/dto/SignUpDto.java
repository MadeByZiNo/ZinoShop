package com.JH.JhOnlineJudge.user.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {

    @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문, 숫자만 사용 가능합니다.")
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
