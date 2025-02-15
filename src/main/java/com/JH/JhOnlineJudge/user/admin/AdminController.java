package com.JH.JhOnlineJudge.user.admin;

import com.JH.JhOnlineJudge.category.CategoryService;
import com.JH.JhOnlineJudge.category.domain.Category;
import com.JH.JhOnlineJudge.user.admin.statistic.ProductSalesRankDto;
import com.JH.JhOnlineJudge.user.admin.statistic.ProductSalesStatDto;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.service.ProductService;
import com.JH.JhOnlineJudge.user.admin.domain.Admin;
import com.JH.JhOnlineJudge.user.admin.dto.DeliverySearchFilterDto;
import com.JH.JhOnlineJudge.user.admin.dto.DeliverySearchResponseDto;
import com.JH.JhOnlineJudge.user.admin.dto.DeliveryStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
       public String getAdminProductForm(@Admin Long userId,
                                   Model model) {
        List<Product> products = productService.getAllProducts();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("products",products);
        model.addAttribute("categories",categories);

        return "admin/product";
    }

    @GetMapping("/analytics-form")
       public String getAdminAnalyticsForm(@Admin Long userId,
                                   Model model) {
        model.addAttribute("products",productService.getAllProducts());
        return "admin/analytics";
    }

    @GetMapping("/deliveries")
    @ResponseBody
    public ResponseEntity<List<DeliverySearchResponseDto>> getDeliveryList(@Admin Long userId,
                                                                           @ModelAttribute DeliverySearchFilterDto request,
                                                                           Model model) {
        List<DeliverySearchResponseDto> deliveries = adminService.getDeliveries(request);
        System.out.println(deliveries.size());
        return  ResponseEntity.ok(deliveries);
    }

    @PutMapping("/deliveries")
    public ResponseEntity<?> updateDeliveryStatus(@RequestBody DeliveryStatusUpdateDto request) {
        List<Long> deliveryIds = request.getDeliveryIds();
        String status = request.getStatus();

        adminService.updateDeliveryStatus(deliveryIds, OrderStatus.valueOf(status));

        return ResponseEntity.ok().build();
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
                                                                          @RequestParam(required = false) Integer day) {
        List<ProductSalesRankDto> response = new ArrayList<>();

        if ("daily".equals(timeFilter)) {
            response = adminService.getDailyStatistics(year, month, day);
        } else if ("monthly".equals(timeFilter)) {
            response = adminService. getMonthlyStatistics(year, month);
        } else if ("yearly".equals(timeFilter)) {
            response = adminService.getYearlyStatistics(year);
        }
        return ResponseEntity.ok(response);
    }
}
