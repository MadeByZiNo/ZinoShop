package com.JH.JhOnlineJudge.review.service;

import com.JH.JhOnlineJudge.common.Image.ProductImage.ProductImage;
import com.JH.JhOnlineJudge.common.Image.ReviewImage.ReviewImage;
import com.JH.JhOnlineJudge.common.Image.ReviewImage.ReviewImageRepository;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.service.ProductService;
import com.JH.JhOnlineJudge.review.domain.Review;
import com.JH.JhOnlineJudge.review.dto.ReviewCreateDto;
import com.JH.JhOnlineJudge.review.dto.ReviewListDto;
import com.JH.JhOnlineJudge.review.exception.DuplicateReviewException;
import com.JH.JhOnlineJudge.review.exception.NotFoundReviewException;
import com.JH.JhOnlineJudge.review.exception.ReviewPermissionException;
import com.JH.JhOnlineJudge.review.repository.ReviewRepository;
import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.service.UserService;
import com.JH.JhOnlineJudge.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.valves.RemoteIpValve;
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
    public Page<ReviewListDto> getReviews(Long productId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviewPage = reviewRepository.findByProductId(productId, pageRequest);

        List<ReviewListDto> reviewDtoList = reviewPage.getContent().stream()
                .map(review -> ReviewListDto.of(review.getId(), review.getUser().getNickname(), review.getCreatedAt(), review.getContent(), review.getImages().isEmpty() ? null : review.getImages().get(0).getUrl()))
                .collect(Collectors.toList());

        return new PageImpl<>(reviewDtoList, pageRequest, reviewPage.getTotalElements());
    }

    @Transactional
    public List<String> getReviewImages(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(NotFoundReviewException::new);
        return review.getImages()
                .stream()
                .map(image->image.getUrl())
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<ReviewListDto> getUserReviews(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviewPage = reviewRepository.findByUserId(userId, pageRequest);

        List<ReviewListDto> reviewDtoList = reviewPage.getContent().stream()
                      .map(review -> ReviewListDto.of(review.getId(), review.getUser().getNickname(), review.getCreatedAt(), review.getContent(), review.getImages().isEmpty() ? null : review.getImages().get(0).getUrl()))
                      .collect(Collectors.toList());
       return new PageImpl<>(reviewDtoList, pageRequest, reviewPage.getTotalElements());
      }

      @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    private String getFirstImageUrl(Review review) {
           return review.getImages().isEmpty() ? null : review.getImages().get(0).getUrl();
    }

    @Transactional
    public boolean checkReviewPermission(Long userId, Long productId) {

        if(reviewRepository.existsByUserIdAndProductId(userId, productId)){throw new DuplicateReviewException();}
        User user = userService.findUserById(userId);
        Product product = productService.findProductById(productId);

        return user.getOrders().stream()
                .filter(order -> order.getStatus().name().equals("배송완료"))
                .anyMatch(order -> order.getOrderProducts().stream()
                        .anyMatch(orderProduct -> orderProduct.getProduct().getId().equals(product.getId())));

    }

    @Transactional(rollbackFor = Exception.class)
    public Review createReview(Long userId, ReviewCreateDto createDto, MultipartFile[] files) {
        User user = userService.findUserById(userId);
        Product product = productService.findProductById(createDto.getProductId());
        Review review = Review.of(user,product,createDto.getContent());
        reviewRepository.save(review);


         List<ReviewImage> imageList = new ArrayList<>();

         if (files != null && files.length > 0){
             for (MultipartFile file : files) {
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
