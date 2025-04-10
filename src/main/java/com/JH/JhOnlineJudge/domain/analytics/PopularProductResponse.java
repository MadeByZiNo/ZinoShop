package com.JH.JhOnlineJudge.domain.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopularProductResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long productId;
    private String name;
    private String imageUrl;
}
