package com.JH.JhOnlineJudge.domain.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateRequest {

    private String username;

    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다.")
    @NotEmpty(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "배송 주소를 등록해주세요.")
    private String deliveryAddress;

    @NotBlank(message = "상세 주소를 등록해주세요.")
   private String detailAddress;

    @NotEmpty(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    private String newPassword;

    @AssertTrue(message = "새 비밀번호는 8자 이상 20자 이하여야 하며, 영문 대문자, 숫자, 특수문자가 하나씩 있어야 합니다.")
     public boolean isNewPasswordValid() {
       if (newPassword == null || newPassword.isEmpty()) {
           return true;
       }
       return newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$");
   }


    @Builder
    private UpdateRequest(String username, String nickname, String deliveryAddress, String detailAddress) {
        this.username = username;
       this.nickname = nickname;
       this.deliveryAddress = deliveryAddress;
       this.detailAddress = detailAddress;
    }

    public static UpdateRequest of(String username, String nickname, String deliveryAddress, String detailAddress) {
        return UpdateRequest.builder()
                .username(username)
                .nickname(nickname)
                .deliveryAddress(deliveryAddress)
                .detailAddress(detailAddress)
                .build();
    }


}
