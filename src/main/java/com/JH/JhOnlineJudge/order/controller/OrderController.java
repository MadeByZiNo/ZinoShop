package com.JH.JhOnlineJudge.order.controller;

import com.JH.JhOnlineJudge.cart.service.CartService;
import com.JH.JhOnlineJudge.cart.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.coupon.dto.CouponResponse;
import com.JH.JhOnlineJudge.coupon.service.CouponService;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.dto.MyOrdersResponse;
import com.JH.JhOnlineJudge.order.dto.OrderConfirmRequest;
import com.JH.JhOnlineJudge.order.dto.OrderDetailRequest;
import com.JH.JhOnlineJudge.order.dto.OrderSaveRequest;
import com.JH.JhOnlineJudge.order.service.OrderService;
import com.JH.JhOnlineJudge.user.domain.AuthUser;
import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final CouponService couponService;

    @Value("${toss.client-key}")
    private String CLIENT_KEY;

    @GetMapping("/page")
    public String orderPage(@AuthUser Long userId,
                       Model model) {
        User user = userService.findUserById(userId);
        List<CartProduct> cartProducts = cartService.getProductsInCart(userId);

        int totalPrice = CartProduct.sumPricesFromList(cartProducts);

        String orderName = cartProducts.get(0).getProduct().getName();
        if(cartProducts.size()>1) {
            orderName += " 외 " + (cartProducts.size() - 1) + "개";
        }

        List<CouponResponse> coupons = couponService.getAvailableCouponsByUserId(userId);
        coupons.stream().forEach(coupon -> System.out.println(coupon.isUsed()));
        model.addAttribute("user",user);
        model.addAttribute("cartProducts",cartProducts);
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("clientKey",CLIENT_KEY);
        model.addAttribute("orderName",orderName);
        model.addAttribute("coupons",coupons);

        return "cart/order";
   }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createOrder(@AuthUser Long userId,
                                      @RequestBody OrderSaveRequest orderSaveRequest){
        List<CartProduct> cartProducts = cartService.getProductsInCart(userId);
        Map<String, Object> map = orderService.createOrder(userId, orderSaveRequest, cartProducts);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam(name = "orderId") String externalId,
                                @RequestParam String paymentKey,
                                @RequestParam int amount,
                                Model model) {
      model.addAttribute("paymentKey", paymentKey);
      model.addAttribute("externalId", externalId);
      model.addAttribute("amount", amount);
      log.info("결제 요청 성공 : paymentKey: {}",paymentKey);
       return "order/success";
    }

    @GetMapping("/fail")
    public String paymentFail(@RequestParam(required = false) String code,
                             @RequestParam(required = false) String message,
                             @RequestParam(required = false) String externalId) {
       Order order = orderService.getOrderByExternalId(externalId);
       order.updateCancel();
       log.info("결제 요청 실패 {} {} ",code,message);
       return "order/fail";
    }

    @PostMapping("/confirm")
    @ResponseBody
     public ResponseEntity<Map<String, Object>> confirmPayment(//@AuthUser Long userId,
                                                               @RequestBody OrderConfirmRequest orderConfirmRequest) {
         Map<String, Object> response = orderService.confirmPaymentAndUpdate(orderConfirmRequest);
         log.info("response ={}",response.values());
         return ResponseEntity.ok(response);
     }

    @GetMapping("/my-page")
    public String getOrderListPage(@AuthUser Long userId,
                                   Model model) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        model.addAttribute("orders",orders);
        return "users/orders";
    }

    @GetMapping("/my-order")
    @ResponseBody
     public ResponseEntity<List<MyOrdersResponse>> getOrderList(@AuthUser Long userId,
                                                                Model model) {
        List<MyOrdersResponse> response = orderService.getUserOrders(userId);
         return ResponseEntity.ok(response);
     }

    @GetMapping("/{orderId}")
    @ResponseBody
    public ResponseEntity<OrderDetailRequest> getOrderDetail(@AuthUser Long userId,
                                                             @PathVariable Long orderId) {
        OrderDetailRequest response = orderService.getOrderDetailRequestDto(orderId);
        return ResponseEntity.ok(response);
    }
}
