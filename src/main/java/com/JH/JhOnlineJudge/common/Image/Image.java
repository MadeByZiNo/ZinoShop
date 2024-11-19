package com.JH.JhOnlineJudge.common.Image;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@MappedSuperclass
@Getter
public abstract class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String url;

    public Image(String url) {
        this.url = url;
    }
}