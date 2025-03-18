package com.JH.JhOnlineJudge.order.service;

import com.JH.JhOnlineJudge.cart.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.order.OrderProduct.OrderProduct;
import com.JH.JhOnlineJudge.coupon.domain.Coupon;
import com.JH.JhOnlineJudge.coupon.service.CouponService;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import com.JH.JhOnlineJudge.order.dto.*;
import com.JH.JhOnlineJudge.order.exception.InsufficientRemainException;
import com.JH.JhOnlineJudge.order.exception.InvalidOrderException;
import com.JH.JhOnlineJudge.order.exception.NotFoundOrderException;
import com.JH.JhOnlineJudge.order.repository.OrderRepository;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.service.ProductService;
import com.JH.JhOnlineJudge.user.admin.dto.DeliverySearchFilterDto;
import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.exception.InvalidRewardPointsException;
import com.JH.JhOnlineJudge.user.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    private final CouponService couponService;
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    private static int COUNT = 0;

    @Value("${toss.secret-key}")
     private String SECRET_KEY;

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                        .orElseThrow(NotFoundOrderException::new);
                return order;
    }

    @Transactional(readOnly = true)
    public Order getOrderByExternalId(String externalId) {
        Order order = orderRepository.findByExternalId(externalId)
                .orElseThrow(NotFoundOrderException::new);
        return order;
    }


    @Transactional(readOnly = true)
    public List<Order> searchOrdersByStatusAndText(DeliverySearchFilterDto deliverySearchFilterDto) {
        List<Order> searchOrders = orderRepository.findByStatusAndSearchText(
              deliverySearchFilterDto.getStatus().equals("") ? null : OrderStatus.valueOf(deliverySearchFilterDto.getStatus()),
              deliverySearchFilterDto.getSearch());
        return searchOrders;
    }

    @Transactional
    public Map<String,Object> createOrder(Long userId,
                                       OrderSaveRequest orderSaveRequest,
                                       List<CartProduct> cartProducts) {

        User user = userService.findUserById(userId);

        Coupon coupon = null;
        if(orderSaveRequest.getCouponId() != null) {
           coupon  = couponService.getCouponById(orderSaveRequest.getCouponId());
           if(coupon.isUsed()){ throw new InsufficientRemainException("이미 사용된 쿠폰입니다."); }
           if(coupon.getMinAmount() > CartProduct.sumPricesFromList(cartProducts)){throw new InsufficientRemainException("최소금액이 충족되지 않았습니다.");}
        }

        Order order = Order.of(user, orderSaveRequest, cartProducts, coupon);

        // 포인트 개수 예외처리
        if(orderSaveRequest.getRewardPointsDiscountPrice() > user.getRewardPoints() || orderSaveRequest.getDiscountedPrice() < 0) {
            throw new InvalidRewardPointsException();
        }

       for (CartProduct cartProduct : cartProducts) {
           Product product = productService.getProductById(cartProduct.getProduct().getId());

           int quantity = cartProduct.getQuantity();
           int price = product.getPrice();

           // 수량부족의 경우 예외 처리
          if (product.getRemain() < quantity  || product.getState().name().equals("품절")) {
              throw new InsufficientRemainException("품절 된 상품입니다.");
          }

            OrderProduct orderProduct = OrderProduct.of(order,product,quantity,price);
            orderProduct.attachOrder(order);
       }

       // 임시 주문서 저장
       orderRepository.save(order);

       Map<String ,Object> map = new HashMap<>();
       map.put("externalId", order.getExternalId());
       map.put("amount", order.getFinalPrice());
       map.put("orderName",order.getName());
       map.put("recipientName",order.getRecipientName());
       return map;
    }

    @Retryable(
            retryFor = {OptimisticLockException.class, StaleObjectStateException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Map<String, Object> confirmPaymentAndUpdate(OrderConfirmRequest orderConfirmRequest) {

        Order order = getOrderByExternalId(orderConfirmRequest.getExternalId());

      /*  if(order.getStatus() != OrderStatus.결제전) {
            throw new InvalidOrderException("적절하지 않은 주문 요청입니다.");
        }*/

        User user = order.getUser();
        Coupon coupon = order.getCoupon();

        // 포인트 개수 예외처리
        if(order.getRewardPointsDiscountPrice() > user.getRewardPoints() || order.getDiscountedPrice() < 0) {
            throw new InvalidRewardPointsException();
        }
        // 포인트 업데이트
        int point = user.updateRewardPoints(order);

        // 재고 차감
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = orderProduct.getProduct();
            int quantity = orderProduct.getQuantity();

            // 품절일 경우 예외 처리
            if (product.getRemain() < orderProduct.getQuantity() || product.getState().name().equals("품절")) {
                throw new InsufficientRemainException(user.getNickname() + "   품절 된 상품입니다.");
            }

            // 재고 차감
            product.updateRemain(-quantity);
            System.out.println("QUANTITY : " + product.getRemain());
        }

        // 쿠폰 개수 예외처리
    /*    if(coupon != null) {
            if(coupon.isUsed()) {throw new InsufficientRemainException(user.getNickname() +    "이미 사용된 쿠폰입니다.");}
            // 쿠폰 차감
            coupon.updateUsed();
        }*/

        /*/ 테스트전용(실제 결제한 데이터가 없기에 race condition 체크
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("result", true);
        responseData.put("point", point);
        System.out.println("COUNT : " + ++COUNT);
        return responseData;
*/
        // 테스트 끝나면 주석풀기

        // 결제 확정 요청 후 정보 추출
        //ResponseEntity<String> response = requestPaymentVerification(orderConfirmRequest);
        //ExtractPaymentDto paymentInfo = extractPaymentInfo(response.getBody());

        // 결제 확정 처리
        order.updateConfirm(order.getPaymentKey(), order.getPaymentMethod());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("result", true);
        responseData.put("point", point);
        return responseData;


    }


    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public List<MyOrdersResponse> getUserOrders(Long userId) {
        List<Order> orders = getOrdersByUserId(userId);
        List<MyOrdersResponse> responses = orders.stream()
                .map(order -> MyOrdersResponse.of(order.getExternalId(), order.getOrderAt(), order.getName(), order.getStatus(), order.getRecipientName(),
                        order.getRecipientAddress(), order.getTotalPrice()))
                .collect(Collectors.toList());
        return responses;
    }

    @Transactional
    private ResponseEntity<String> requestPaymentVerification(OrderConfirmRequest orderConfirmRequest) {
          HttpHeaders headers = new HttpHeaders();
          headers.setBasicAuth(SECRET_KEY, "");
          headers.setContentType(MediaType.APPLICATION_JSON);

          Map<String, Object> requestBody = Map.of(
              "paymentKey", orderConfirmRequest.getPaymentKey(),
              "orderId", orderConfirmRequest.getExternalId(),
              "amount", orderConfirmRequest.getAmount()
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
      public OrderDetailRequest getOrderDetailRequestDto(Long orderId) {
          Order order = orderRepository.findOrderWithProducts(orderId);

          List<OrderProductDetailDto> products = order.getOrderProducts()
                  .stream()
                  .map(orderProduct -> new OrderProductDetailDto(
                          orderProduct.getProduct().getName(),
                          orderProduct.getQuantity(),
                          orderProduct.getProduct().getPrice()))
                  .collect(Collectors.toList());

          return OrderDetailRequest.of(order, products);
      }


    private ExtractPaymentDto extractPaymentInfo(String responseBody) {
          try {
              JsonNode jsonResponse = new ObjectMapper().readTree(responseBody);
              String externalId = jsonResponse.get("orderId").asText();
              String paymentKey = jsonResponse.get("paymentKey").asText();
              String method = jsonResponse.get("method").asText();

              return new ExtractPaymentDto(externalId, paymentKey, method);
          } catch (Exception e) {
              throw new RuntimeException("결제 정보 추출 실패", e);
          }
      }


}
