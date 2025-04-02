package com.JH.JhOnlineJudge.domain.heart.controller;

import com.JH.JhOnlineJudge.domain.heart.service.HeartService;
import com.JH.JhOnlineJudge.domain.heart.entity.Heart;
import com.JH.JhOnlineJudge.domain.user.auth.AuthUser;
import com.JH.JhOnlineJudge.domain.user.auth.IsLogin;
import com.JH.JhOnlineJudge.domain.user.dto.IsLoginRequest;
import com.JH.JhOnlineJudge.domain.user.exception.LoginInvalidException;
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
    public ResponseEntity<Map<String, Boolean>> toggleHeartStatus(@IsLogin IsLoginRequest isLoginRequest,
                                            @RequestBody Map<String, Long> requestId) {

        if(!isLoginRequest.getIsLogin()) {
            throw new LoginInvalidException();
        }

        Long userId = isLoginRequest.getUserId();
        Long productId = requestId.get("productId");

        boolean isHearted = heartService.toggleHeart(userId, productId);

        return ResponseEntity.ok(Collections.singletonMap("isFavorite", isHearted));
    }

    @GetMapping("/{productId}/status")
    @ResponseBody
    public ResponseEntity<Boolean> isProductHearted(@IsLogin IsLoginRequest isLoginRequest,
                                                    @PathVariable Long productId) {

        if(!isLoginRequest.getIsLogin()){
            return ResponseEntity.ok(false);
        }

        Long userId = isLoginRequest.getUserId();

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
