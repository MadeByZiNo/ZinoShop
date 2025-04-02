package com.JH.JhOnlineJudge.domain.review.service;

import com.JH.JhOnlineJudge.domain.Image.ImageFrom;
import com.JH.JhOnlineJudge.domain.Image.ImageUploadService;
import com.JH.JhOnlineJudge.domain.Image.ReviewImage.ReviewImage;
import com.JH.JhOnlineJudge.domain.Image.ReviewImage.ReviewImageRepository;
import com.JH.JhOnlineJudge.domain.product.entity.Product;
import com.JH.JhOnlineJudge.domain.product.service.ProductService;
import com.JH.JhOnlineJudge.domain.review.entity.Review;
import com.JH.JhOnlineJudge.domain.review.dto.ReviewCreateRequest;
import com.JH.JhOnlineJudge.domain.review.dto.ReviewListResponse;
import com.JH.JhOnlineJudge.domain.review.exception.DuplicateReviewException;
import com.JH.JhOnlineJudge.domain.review.exception.NotFoundReviewException;
import com.JH.JhOnlineJudge.domain.review.repository.ReviewRepository;
import com.JH.JhOnlineJudge.domain.user.entity.User;
import com.JH.JhOnlineJudge.domain.user.service.UserService;
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
    private final ImageUploadService imageUploadService;

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

        if (images != null){
            for (MultipartFile file : images) {
               imageUploadService.uploadAndSaveReviewImage(file, ImageFrom.REVIEW.getDir(), review);
            }
        }

        return review;
    }


}
