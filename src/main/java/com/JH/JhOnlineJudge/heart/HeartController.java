package com.JH.JhOnlineJudge.heart;

import com.JH.JhOnlineJudge.category.domain.Category;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.user.domain.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/heart")
public class HeartController {

    private final HeartService  heartService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<Map> updateHeart(@AuthUser Long userId,
                                            @RequestBody Map<String, Long> request) {
        Long productId = request.get("productId");
        boolean isHearted = heartService.updateHeart(userId, productId);

        return ResponseEntity.ok(Collections.singletonMap("isFavorite", isHearted));
    }

     @GetMapping("/page")
     public String getHeartListPage(@AuthUser Long userId,
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

    @GetMapping("/status")
    @ResponseBody
    public ResponseEntity<Boolean> checkHeartStatus(@AuthUser Long userId, @RequestParam Long productId) {
        boolean isHearted = heartService.existsHeart(userId, productId);
        log.info("{}{}{}",userId,productId,isHearted);
        return ResponseEntity.ok(isHearted);
    }
}
