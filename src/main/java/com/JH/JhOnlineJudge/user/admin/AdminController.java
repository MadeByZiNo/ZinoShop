package com.JH.JhOnlineJudge.user.admin;

import com.JH.JhOnlineJudge.order.domain.OrderStatus;
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

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String getAdminPageForm(@Admin Long userId,
                                Model model) {
        return "admin/main";
    }
    @GetMapping("/delivery-form")
      public String getAdminDeliveryPageForm(@Admin Long userId,
                                  Model model) {
          return "admin/delivery";
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
}
