package com.JH.JhOnlineJudge.domain.user.admin;

import com.JH.JhOnlineJudge.domain.category.CategoryService;
import com.JH.JhOnlineJudge.domain.category.entity.Category;
import com.JH.JhOnlineJudge.domain.product.statistic.ProductSalesRankDto;
import com.JH.JhOnlineJudge.domain.product.statistic.ProductSalesStatDto;
import com.JH.JhOnlineJudge.domain.order.entity.OrderStatus;
import com.JH.JhOnlineJudge.domain.product.entity.Product;
import com.JH.JhOnlineJudge.domain.product.service.ProductService;
import com.JH.JhOnlineJudge.domain.user.admin.domain.Admin;
import com.JH.JhOnlineJudge.domain.user.admin.dto.DeliverySearchFilterDto;
import com.JH.JhOnlineJudge.domain.user.admin.dto.DeliverySearchResponseDto;
import com.JH.JhOnlineJudge.domain.user.admin.dto.DeliveryStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final ProductService productService;
    private final CategoryService categoryService;
    @GetMapping
    public String getAdminForm(@Admin Long userId,
                                Model model) {
        return "admin/main";
    }

    @GetMapping("/delivery-form")
      public String getAdminDeliveryForm(@Admin Long userId,
                                  Model model) {
          return "admin/delivery";
      }

    @GetMapping("/product-form")
    public String getProductPage(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model
    ) {

        List<Long> categoryIds = categoryId != null ? (List<Long>) categoryService.getCategoryIdsWithParentCategory(categoryId).get("categoryIds") : null;
        System.out.println("category ids = " + categoryIds);


        Slice<Product> products = productService.searchProducts(categoryIds, name, page, size);
        List<Category> categories = categoryService.findAll();

        model.addAttribute("products", products.getContent());
        model.addAttribute("hasNext", products.hasNext());
        model.addAttribute("currentPage", page);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("searchName", name);

        return "admin/product";
    }

    @GetMapping("/product-form-fragment")
    public String getProductFragment(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model
    ) {

        List<Long> categoryIds = categoryId != null ? (List<Long>) categoryService.getCategoryIdsWithParentCategory(categoryId).get("categoryIds") : null;
        System.out.println("category ids = " + categoryIds);
        Slice<Product> products = productService.searchProducts(categoryIds, name, page, size);
        model.addAttribute("products", products.getContent());
        return "admin/product :: productRows";
    }


    @GetMapping("/deliveries")
    @ResponseBody
    public ResponseEntity<Slice<DeliverySearchResponseDto>> getDeliveryList(
            @Admin Long userId,
            @ModelAttribute DeliverySearchFilterDto request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Slice<DeliverySearchResponseDto> deliveries = adminService.getDeliveries(request, page, size);
        return ResponseEntity.ok(deliveries);
    }

    @PutMapping("/deliveries")
    public ResponseEntity<?> updateDeliveryStatus(@RequestBody DeliveryStatusUpdateDto request) {
        List<Long> deliveryIds = request.getDeliveryIds();
        String status = request.getStatus();

        adminService.updateDeliveryStatus(deliveryIds, OrderStatus.valueOf(status));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/analytics-form")
    public String getAdminAnalyticsForm(@Admin Long userId) {
        return "admin/analytics";
    }

    @GetMapping("/statistic/product")
    public ResponseEntity<List<ProductSalesStatDto>> getProductSalesStatistic(@Admin Long userId,
                                                                              @RequestParam(name = "timeFilter") String timeFilter,
                                                                              @RequestParam(name = "productId") Long productId,
                                                                              @RequestParam(name = "year", required = false) Integer year,
                                                                              @RequestParam(name = "month", required = false) Integer month) {

        List<ProductSalesStatDto> response = new ArrayList<>();

        if ("daily".equals(timeFilter)) {
            response = adminService.getDailyProductSalesStatistic(productId, year, month);
          } else if ("monthly".equals(timeFilter)) {
            response = adminService. getMonthlyProductSalesStatistic(productId, year);
          } else if ("yearly".equals(timeFilter)) {
            response = adminService.getYearlyProductSalesStatistic(productId);
          }
        log.info("response : {}   {}",response.size(), response.getFirst().getDate());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistic/rank")
    public ResponseEntity<List<ProductSalesRankDto>> getRankingStatistics(@Admin Long userId,
                                                                          @RequestParam String timeFilter,
                                                                          @RequestParam(required = false) Integer year,
                                                                          @RequestParam(required = false) Integer month,
                                                                          @RequestParam(required = false) Integer day,
                                                                          @RequestParam(required = true) String sortType) {
        List<ProductSalesRankDto> response = new ArrayList<>();

        if ("daily".equals(timeFilter)) {
            response = adminService.getDailyStatistics(year, month, day, sortType);
        } else if ("monthly".equals(timeFilter)) {
            response = adminService.getMonthlyStatistics(year, month, sortType);
        } else if ("yearly".equals(timeFilter)) {
            response = adminService.getYearlyStatistics(year, sortType);
        }
        return ResponseEntity.ok(response);
    }
}
