package com.JH.JhOnlineJudge.order.controller;

import com.JH.JhOnlineJudge.cart.service.CartService;
import com.JH.JhOnlineJudge.common.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.dto.OrderConfirmDto;
import com.JH.JhOnlineJudge.order.dto.OrderSaveDto;
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

    @Value("${toss.client-key}")
    private String CLIENT_KEY;

    @GetMapping("/page")
    public String orderPage(@AuthUser Long userId,
                       Model model) {
        User user = userService.findUserById(userId);
        List<CartProduct> cartProducts = cartService.getCartProductList(userId);

        int totalPrice = CartProduct.sumPricesFromList(cartProducts);

        String orderName = cartProducts.get(0).getProduct().getName();
        if(cartProducts.size()>1) {
            orderName += " 외 " + (cartProducts.size() - 1) + "개";
        }

        model.addAttribute("user",user);
        model.addAttribute("cartProducts",cartProducts);
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("clientKey",CLIENT_KEY);
        model.addAttribute("orderName",orderName);
        return "cart/order";
   }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> saveOrder(@AuthUser Long userId,
                                      @RequestBody OrderSaveDto orderSaveDto){
        List<CartProduct> cartProducts = cartService.getCartProductList(userId);
        Map<String, Object> map = orderService.createOrder(userId, orderSaveDto, cartProducts);
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
           Order order = orderService.findByExternalId(externalId);
           order.updateCancel();
           log.info("결제 요청 실패 {} {} ",code,message);
           return "order/fail";
       }

    @PostMapping("/confirm")
     public ResponseEntity<Map<String, Object>> confirmPayment(@AuthUser Long userId,
                                                               @RequestBody OrderConfirmDto orderConfirmDto) {
         Map<String, Object> responseData = orderService.confirmPaymentAndUpdate(userId, orderConfirmDto);
         return ResponseEntity.ok(responseData);
     }


}
