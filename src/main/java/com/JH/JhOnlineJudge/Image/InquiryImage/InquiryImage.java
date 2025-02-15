package com.JH.JhOnlineJudge.Image.InquiryImage;

import com.JH.JhOnlineJudge.Image.Image;
import com.JH.JhOnlineJudge.inquiry.domain.Inquiry;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class InquiryImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", nullable = false)
    private Inquiry inquiry;

    @Builder
    public InquiryImage(String url, Inquiry inquiry) {
        super(url);
        attachReview(inquiry);
    }

    public void attachReview(Inquiry inquiry) {
        this.inquiry = inquiry;
        inquiry.getImages().add(this);
    }
}