package com.JH.JhOnlineJudge.domain.user.admin.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeliveryStatusUpdateDto {

    private List<Long> deliveryIds;
    private String status;
}
