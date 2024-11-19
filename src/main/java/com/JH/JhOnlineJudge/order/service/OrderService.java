package com.JH.JhOnlineJudge.order.service;

import com.JH.JhOnlineJudge.common.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.common.OrderProduct.OrderProduct;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import com.JH.JhOnlineJudge.order.dto.*;
import com.JH.JhOnlineJudge.order.exception.InsufficientRemainException;
import com.JH.JhOnlineJudge.order.exception.NotFoundOrderException;
import com.JH.JhOnlineJudge.order.repository.OrderRepository;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.service.ProductService;
import com.JH.JhOnlineJudge.user.admin.dto.DeliverySearchFilterDto;
import com.JH.JhOnlineJudge.user.domain.AuthUser;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserService userService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${toss.secret-key}")
     private String SECRET_KEY;

    @Transactional
    public Order findById(Long id) {
        Order order = orderRepository.findById(id)
                        .orElseThrow(NotFoundOrderException::new);
                return order;
    }

    @Transactional
    public Order findByExternalId(String externalId) {
        Order order = orderRepository.findByExternalId(externalId)
                .orElseThrow(NotFoundOrderException::new);
        return order;
    }


    @Transactional
    public List<Order> findByStatusAndSearchText(DeliverySearchFilterDto deliverySearchFilterDto) {
        List<Order> searchOrders = orderRepository.findByStatusAndSearchText(
              deliverySearchFilterDto.getStatus().equals("") ? null : OrderStatus.valueOf(deliverySearchFilterDto.getStatus()),
              deliverySearchFilterDto.getSearch());
        return searchOrders;
    }

    @Transactional
    public Map<String,Object> createOrder(Long userId,
                                       OrderSaveDto orderSaveDto,
                                       List<CartProduct> cartProducts) {

        User user = userService.findUserById(userId);

        if(orderSaveDto.getDiscountedPrice() > user.getRewardPoints() || orderSaveDto.getDiscountedPrice() < 0) {
            throw new InvalidRewardPointsException();
        }

        Order order = Order.of(user, orderSaveDto, cartProducts);

       for (CartProduct cartProduct : cartProducts) {
           Product product = productService.findProductByIdWithLock(cartProduct.getProduct().getId());
           int quantity = cartProduct.getQuantity();
           int price = product.getPrice();

          if (product.getRemain() <= 0 || product.getState().equals("품절")) {
              throw new InsufficientRemainException("품절 된 상품입니다."); // 재고 부족 예외 발생
          }

          // 주문 수량이 재고 수량보다 많을 경우 예외 처리
          if (product.getRemain() < quantity) {
              throw new InsufficientRemainException("재고 수량보다 많은 구매 요청을 하셨습니다."); // 재고 부족 예외 발생
          }

           OrderProduct orderProduct = OrderProduct.of(order,product,quantity,price);
           orderProduct.attachOrder(order);
       }
       orderRepository.save(order);

       Map<String ,Object> map = new HashMap<>();
       map.put("externalId", order.getExternalId());
       map.put("amount", order.getFinalPrice());
       map.put("orderName",order.getName());
       map.put("recipientName",order.getRecipientName());
       return map;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> confirmPaymentAndUpdate(Long userId, OrderConfirmDto orderConfirmDto) {

        //재고 확인
         Order order = findByExternalId(orderConfirmDto.getExternalId());
          for (OrderProduct orderProduct : order.getOrderProducts()) {
              Product product = productService.findProductByIdWithLock(orderProduct.getProduct().getId());

              if (product.getRemain() <= 0 || product.getState().equals("품절")) {
                         throw new InsufficientRemainException("품절 된 상품입니다."); // 재고 부족 예외 발생
              }

              if (product.getRemain() < orderProduct.getQuantity()) {
                  throw new InsufficientRemainException("재고 수량보다 많은 구매 요청을 하셨습니다."); // 재고 부족 예외 발생
              }
          }

          // 결제 확정 요청 후 정보 추출
         ResponseEntity<String> response = requestPaymentConfirmation(orderConfirmDto);
         extractPaymentDto paymentInfo = extractPaymentInfo(response.getBody());

         // 결제 확정 처리
         order.updateConfirm(paymentInfo.getPaymentKey(), paymentInfo.getMethod());
         updateProductRemain(order);

         // 포인트 업데이트
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

    @Transactional
    public List<Order> findOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public List<MyOrdersDto> getMyOrderDtoList(Long userId) {
        List<Order> orders = findOrdersByUserId(userId);
        List<MyOrdersDto> dtoList = orders.stream()
                .map(order -> MyOrdersDto.of(order.getExternalId(), order.getOrderAt(), order.getName(), order.getStatus(), order.getRecipientName(),
                        order.getRecipientAddress(), order.getTotalPrice()))
                .collect(Collectors.toList());
        return dtoList;
    }
    @Transactional
    private ResponseEntity<String> requestPaymentConfirmation(OrderConfirmDto orderConfirmDto) {
          HttpHeaders headers = new HttpHeaders();
          headers.setBasicAuth(SECRET_KEY, "");
          headers.setContentType(MediaType.APPLICATION_JSON);

          Map<String, Object> requestBody = Map.of(
              "paymentKey", orderConfirmDto.getPaymentKey(),
              "orderId", orderConfirmDto.getExternalId(),
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


      @Transactional
      public OrderDetailRequestDto getOrderDetailRequestDto(Long orderId) {
          Order order = findById(orderId);
          List<OrderProductDetailDto> products = order.getOrderProducts()
              .stream()
              .map(orderProduct -> new OrderProductDetailDto(
                  orderProduct.getProduct().getName(),
                  orderProduct.getQuantity(),
                  orderProduct.getProduct().getPrice()))
              .collect(Collectors.toList());

         OrderDetailRequestDto dto=  OrderDetailRequestDto.of(order,products);
         return dto;
      }


    private extractPaymentDto extractPaymentInfo(String responseBody) {
          try {
              JsonNode jsonResponse = new ObjectMapper().readTree(responseBody);
              String externalId = jsonResponse.get("orderId").asText();
              String paymentKey = jsonResponse.get("paymentKey").asText();
              String method = jsonResponse.get("method").asText();

              return new extractPaymentDto(externalId, paymentKey, method);
          } catch (Exception e) {
              throw new RuntimeException("결제 정보 추출 실패", e);
          }
      }


}
