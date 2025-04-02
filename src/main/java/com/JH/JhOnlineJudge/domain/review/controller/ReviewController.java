package com.JH.JhOnlineJudge.domain.review.controller;

import com.JH.JhOnlineJudge.domain.review.dto.ReviewCreateRequest;
import com.JH.JhOnlineJudge.domain.review.dto.ReviewListResponse;
import com.JH.JhOnlineJudge.domain.review.exception.ReviewPermissionException;
import com.JH.JhOnlineJudge.domain.review.service.ReviewService;
import com.JH.JhOnlineJudge.domain.user.auth.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final int REVIEWS_PER_PAGE_LATEST = 2;
    private final int REVIEWS_PER_PAGE = 4;


    @GetMapping("/latest")
    @ResponseBody
    public ResponseEntity<Page<ReviewListResponse>> getLatestReviews(@RequestParam Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId, 0, REVIEWS_PER_PAGE_LATEST));
    }

    @GetMapping("/custom")
    @ResponseBody
    public ResponseEntity<Page<ReviewListResponse>> getReviewsPage(@RequestParam Long productId, @RequestParam int page) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId, page, REVIEWS_PER_PAGE));
    }

   @GetMapping("/images")
   @ResponseBody
   public ResponseEntity<List<String>> getReviewImages(@RequestParam Long reviewId) {
       return ResponseEntity.ok(reviewService.getReviewImagesByReviewId(reviewId));
   }

  @GetMapping("/page")
  public String getUserReviewsPage(@AuthUser Long userId,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                Model model) {

      Page<ReviewListResponse> reviews = reviewService.getReviewsByUserId(userId, page-1, REVIEWS_PER_PAGE);
      int totalPages = reviews.getTotalPages();
      int currentPage = reviews.getNumber() + 1;  //
      int pageGroup = (currentPage - 1) / 5;
      int startPage = pageGroup * 5 + 1;
      int endPage = Math.min(startPage + 4, totalPages); // 5개의 페이지 또는 총 페이지까지 표시

      model.addAttribute("currentPage", currentPage);
      model.addAttribute("startPage", startPage);
      model.addAttribute("endPage", endPage);
      model.addAttribute("totalPages", totalPages);
      model.addAttribute("reviews", reviews);
       return "users/review";
  }

    @GetMapping("/write")
    public String getWritePage(@AuthUser Long userId,
                               @RequestParam Long productId,
                               Model model) {
        boolean isPermission = reviewService.checkReviewPermission(userId, productId);
        if(!isPermission){throw new ReviewPermissionException();}
        model.addAttribute("productId",productId);
        return "review/write";
    }


    @PostMapping
    public String createReview(@AuthUser Long userId,
                               @ModelAttribute ReviewCreateRequest request,
                               @RequestParam(value = "files", required = false) MultipartFile[] files) {

        if (files != null && files.length > 5) {
            throw new IllegalArgumentException("이미지는 최대 5개까지 첨부할 수 있습니다.");
        }
        reviewService.createReview(userId, request, files);

        return "redirect:/product/review/" + request.getProductId();
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<?> deleteReview(@AuthUser Long userId,
                                     @RequestBody Map<String, Long> request) {
        Long reviewId = request.get("reviewId");
        reviewService.deleteReview(reviewId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
