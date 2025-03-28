package com.JH.JhOnlineJudge.domain.inquiry.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryReplyRequest {
    private Long inquiryId;
    private String reply;
}
