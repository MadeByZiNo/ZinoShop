package com.JH.JhOnlineJudge.domain.inquiry.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquirySaveRequest {
    private String orderId;
    private String title;
    private String content;
}
