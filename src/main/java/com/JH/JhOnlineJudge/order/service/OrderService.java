package com.JH.JhOnlineJudge.order.service;

import com.JH.JhOnlineJudge.common.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.common.OrderProduct.OrderProduct;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.dto.OrderConfirmDto;
import com.JH.JhOnlineJudge.order.dto.OrderSaveDto;
import com.JH.JhOnlineJudge.order.dto.extractPaymentDto;
import com.JH.JhOnlineJudge.order.exception.NotFoundOrderException;
import com.JH.JhOnlineJudge.order.repository.OrderRepository;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.exception.InvalidRewardPointsException;
import com.JH.JhOnlineJudge.user.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserService userService;
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${toss.secret-key}")
     private String SECRET_KEY;

    @Transactional
    public Order findByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(NotFoundOrderException::new);
        return order;
    }

    @Transactional
    public Map<String,Object> createOrder(Long userId,
                                       OrderSaveDto orderSaveDto,
                                       List<CartProduct> cartProducts) {

        User user = userService.findUserById(userId);

        if(orderSaveDto.getDiscountedPrice() > user.getReward_points() || orderSaveDto.getDiscountedPrice() < 0) {
            throw new InvalidRewardPointsException();
        }

        Order order = Order.of(user, orderSaveDto, cartProducts);

       for (CartProduct cartProduct : cartProducts) {
           Product product = cartProduct.getProduct();
           int quantity = cartProduct.getQuantity();
           int price = product.getPrice();

           OrderProduct orderProduct = OrderProduct.of(order,product,quantity,price);
           orderProduct.attachOrder(order);
       }
       orderRepository.save(order);

       Map<String ,Object> map = new HashMap<>();
       map.put("orderId", order.getOrderId());
       map.put("amount", order.getFinal_price());
       map.put("orderName",order.getName());
       map.put("recipient_name",order.getRecipient_name());
       return map;
    }

    public Map<String, Object> confirmPaymentAndUpdate(Long userId, OrderConfirmDto orderConfirmDto) {

         ResponseEntity<String> response = requestPaymentConfirmation(orderConfirmDto);

         extractPaymentDto paymentInfo = extractPaymentInfo(response.getBody());

         Order order = findByOrderId(paymentInfo.getOrderId());
         order.updateConfirm(paymentInfo.getPaymentKey(), paymentInfo.getMethod());
         updateProductRemain(order);

         int point = userService.updateRewardPoints(userId, order);

         Map<String, Object> responseData = new HashMap<>();
         responseData.put("result", response.getStatusCode() == HttpStatus.OK);
         responseData.put("point", point);

         return responseData;
     }


    @Transactional
    public void updateProductRemain(Order order) {
        order.getOrderProducts().stream()
            .forEach(orderProduct -> {
                Product product = orderProduct.getProduct();
                int quantity = orderProduct.getQuantity();
                product.updateRemain(-quantity);
            });
    }


    private ResponseEntity<String> requestPaymentConfirmation(OrderConfirmDto orderConfirmDto) {
          HttpHeaders headers = new HttpHeaders();
          headers.setBasicAuth(SECRET_KEY, "");
          headers.setContentType(MediaType.APPLICATION_JSON);

          Map<String, Object> requestBody = Map.of(
              "paymentKey", orderConfirmDto.getPaymentKey(),
              "orderId", orderConfirmDto.getOrderId(),
              "amount", orderConfirmDto.getAmount()
          );

          HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

          return restTemplate.exchange(
              "https://api.tosspayments.com/v1/payments/confirm",
              HttpMethod.POST,
              request,
              String.class
          );
      }

    private extractPaymentDto extractPaymentInfo(String responseBody) {
          try {
              JsonNode jsonResponse = new ObjectMapper().readTree(responseBody);
              String orderId = jsonResponse.get("orderId").asText();
              String paymentKey = jsonResponse.get("paymentKey").asText();
              String method = jsonResponse.get("method").asText();

              return new extractPaymentDto(orderId, paymentKey, method);
          } catch (Exception e) {
              throw new RuntimeException("결제 정보 추출 실패", e);
          }
      }
}
