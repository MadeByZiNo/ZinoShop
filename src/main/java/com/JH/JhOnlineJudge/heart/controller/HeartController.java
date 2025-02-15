package com.JH.JhOnlineJudge.heart.controller;

import com.JH.JhOnlineJudge.heart.service.HeartService;
import com.JH.JhOnlineJudge.heart.domain.Heart;
import com.JH.JhOnlineJudge.user.domain.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/heart")
public class HeartController {

    private final HeartService heartService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<Map> toggleHeartStatus(@AuthUser Long userId,
                                            @RequestBody Map<String, Long> request) {
        Long productId = request.get("productId");
        boolean isHearted = heartService.toggleHeart(userId, productId);

        return ResponseEntity.ok(Collections.singletonMap("isFavorite", isHearted));
    }

    @GetMapping("/{productId}/status")
    @ResponseBody
    public ResponseEntity<Boolean> isProductHearted(@AuthUser Long userId, @PathVariable Long productId) {
        boolean isHearted = heartService.isHearted(userId, productId);
        return ResponseEntity.ok(isHearted);
    }

     @GetMapping
     public String getHeartsPage(@AuthUser Long userId,
                                   @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                   Model model) {

         Page<Heart> hearts = heartService.getHeartsPageByUserId(userId, page);
         model.addAttribute("hearts", hearts);

         int totalPages = hearts.getTotalPages();
         int currentPage = hearts.getNumber() + 1;  //
         int pageGroup = (currentPage - 1) / 5;
         int startPage = pageGroup * 5 + 1;
         int endPage = Math.min(startPage + 4, totalPages); // 5개의 페이지 또는 총 페이지까지 표시

         model.addAttribute("currentPage", currentPage);
         model.addAttribute("startPage", startPage);
         model.addAttribute("endPage", endPage);
         model.addAttribute("totalPages", totalPages);

         return "users/heart";
     }


}
