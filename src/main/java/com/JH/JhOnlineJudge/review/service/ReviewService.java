package com.JH.JhOnlineJudge.review.service;

import com.JH.JhOnlineJudge.Image.ReviewImage.ReviewImage;
import com.JH.JhOnlineJudge.Image.ReviewImage.ReviewImageRepository;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.service.ProductService;
import com.JH.JhOnlineJudge.review.domain.Review;
import com.JH.JhOnlineJudge.review.dto.ReviewCreateRequest;
import com.JH.JhOnlineJudge.review.dto.ReviewListResponse;
import com.JH.JhOnlineJudge.review.exception.DuplicateReviewException;
import com.JH.JhOnlineJudge.review.exception.NotFoundReviewException;
import com.JH.JhOnlineJudge.review.repository.ReviewRepository;
import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.service.UserService;
import com.JH.JhOnlineJudge.common.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ProductService productService;
    private final ReviewImageRepository reviewImageRepository;
    private final S3Uploader s3Uploader;

    private final String DIR_NAME = "Review";

    @Transactional
    public Page<ReviewListResponse> getReviewsByProductId(Long productId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReviewListResponse> reviewPage = reviewRepository.getReviewDTOByProductId(productId, pageRequest);

        return new PageImpl<>(reviewPage.getContent(), pageRequest, reviewPage.getTotalElements());
    }

    @Transactional
    public Page<ReviewListResponse> getReviewsByUserId(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviewPage = reviewRepository.getByUserIdWithUserAndImages(userId, pageRequest);
        List<ReviewListResponse> reviewDtoList = reviewPage.getContent().stream()
                .map(review -> ReviewListResponse.of(review.getId(), review.getUser().getNickname(), review.getCreatedAt(), review.getContent(), review.getImages().isEmpty() ? null : review.getImages().get(0).getUrl()))
                .collect(Collectors.toList());

        return new PageImpl<>(reviewDtoList, pageRequest, reviewPage.getTotalElements());
    }

    @Transactional
    public List<String> getReviewImagesByReviewId(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(NotFoundReviewException::new);
        return review.getImages()
                .stream()
                .map(image->image.getUrl())
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public boolean checkReviewPermission(Long userId, Long productId) {

        if(reviewRepository.existsByUserIdAndProductId(userId, productId)){throw new DuplicateReviewException();}
        User user = userService.findUserById(userId);
        Product product = productService.getProductById(productId);

        return user.getOrders().stream()
                .filter(order -> order.getStatus().name().equals("구매확정"))
                .anyMatch(order -> order.getOrderProducts().stream()
                        .anyMatch(orderProduct -> orderProduct.getProduct().getId().equals(product.getId())));

    }

    @Transactional(rollbackFor = Exception.class)
    public Review createReview(Long userId, ReviewCreateRequest createDto, MultipartFile[] images) {
        User user = userService.findUserById(userId);
        Product product = productService.getProductById(createDto.getProductId());
        Review review = Review.of(user,product,createDto.getContent());
        reviewRepository.save(review);

        List<ReviewImage> imageList = new ArrayList<>();

        if (images != null && images.length > 0){
            for (MultipartFile file : images) {
               String uploadUrl = s3Uploader.upload(file, DIR_NAME);

               ReviewImage reviewImage = ReviewImage.builder()
                       .url(uploadUrl)
                       .review(review)
                       .build();

               imageList.add(reviewImage);
            }
            reviewImageRepository.saveAll(imageList);
        }

        return review;
    }


}
