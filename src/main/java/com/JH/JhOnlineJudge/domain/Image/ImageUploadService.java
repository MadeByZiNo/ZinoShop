package com.JH.JhOnlineJudge.domain.Image;

import com.JH.JhOnlineJudge.common.utils.S3Uploader;
import com.JH.JhOnlineJudge.domain.Image.InquiryImage.InquiryImage;
import com.JH.JhOnlineJudge.domain.Image.InquiryImage.InquiryImageRepository;
import com.JH.JhOnlineJudge.domain.Image.ProductImage.ProductImage;
import com.JH.JhOnlineJudge.domain.Image.ProductImage.ProductImageRepository;
import com.JH.JhOnlineJudge.domain.Image.ReviewImage.ReviewImage;
import com.JH.JhOnlineJudge.domain.Image.ReviewImage.ReviewImageRepository;
import com.JH.JhOnlineJudge.domain.inquiry.entity.Inquiry;
import com.JH.JhOnlineJudge.domain.product.entity.Product;
import com.JH.JhOnlineJudge.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageUploadService {
    private final S3Uploader s3Uploader;
    private final ProductImageRepository productImageRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final InquiryImageRepository inquiryImageRepository;



    @Async
    public void uploadAndSaveProductImage(MultipartFile file, String dirName, Product product) {
        try {
            String url = s3Uploader.upload(file, dirName);

            ProductImage productImage = ProductImage.builder()
                    .url(url)
                    .product(product)
                    .build();

            productImageRepository.save(productImage);

        } catch (Exception e) {
            log.error("Product 이미지 업로드 실패", e);
        }
    }

    @Async
    public void uploadAndSaveReviewImage(MultipartFile file, String dirName, Review review) {
        try {
            String url = s3Uploader.upload(file, dirName);

            ReviewImage reviewImage = ReviewImage.builder()
                    .url(url)
                    .review(review)
                    .build();

            reviewImageRepository.save(reviewImage);

        } catch (Exception e) {
            log.error("Review 이미지 업로드 실패", e);
        }
    }

    @Async
    public void uploadAndSaveInquiryImage(MultipartFile file, String dirName, Inquiry inquiry) {
        try {
            String url = s3Uploader.upload(file, dirName);

            InquiryImage inquiryImage = InquiryImage.builder()
                    .url(url)
                    .inquiry(inquiry)
                    .build();

            inquiryImageRepository.save(inquiryImage);

        } catch (Exception e) {
            log.error("Inquiry 이미지 업로드 실패", e);
        }
    }
}
