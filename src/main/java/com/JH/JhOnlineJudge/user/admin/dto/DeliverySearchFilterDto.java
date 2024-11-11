package com.JH.JhOnlineJudge.user.admin.dto;


import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliverySearchFilterDto {

    @Nullable
    private String search;

    @Nullable
    private String status;
}
