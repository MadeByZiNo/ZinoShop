# 🛒 ZinoShop 프로젝트

**Spring, Spring Boot, JPA, MySQL**를 기반으로 한 **서버 사이드 렌더링(SSR)** 쇼핑몰 프로젝트. SPRING MVC를 이용하여 JWT를 활용한 인증 및 세션 관리, 상품 관리, 결제 연동 등 다양한 기능을 포함하여 실제 운영중인 쇼핑몰 시스템의 구조를 예상하여 개발해보았습니다.

---

### 개발환경
- **IntelliJ**
- **Postman**
- **GitHub**

### 백엔드
- **Java**
- **Spring**
- **Spring Boot**
- **JPA**

### 데이터베이스
- **MySQL**

### 인프라
- **AWS EC2**
- **AWS S3**
- **Docker**
### CI/CD
- **GitHub Actions**

### 프론트엔드 (프론트는 주로 ChatGpt의 도움을 받았습니다)
- **HTML**
- **JavaScript**
- **Thymeleaf**





### ERD
  ![1](https://github.com/user-attachments/assets/918f56f4-449f-454b-a552-4e14f935766a)  

  


### 아키텍쳐 구조
넣을것것


## 📌 주요 기능  



### 1. 회원 및 인증 관리

- **JWT**를 이용한 로그인 및 세션 관리

- 

![image](https://github.com/user-attachments/assets/e1b052fc-a7ea-470e-beeb-4abf705aad14)  



사용자가 로그인하면 서버는 JWT형태의 AccessToken과 RefreshToken을 발급합니다.  


이 두 토큰은 쿠키에 저장되어 탈취 위험성을 위해 유효 시간을 가집니다.  

 
AccessToken은 인증하는데 사용되며 유혀 시간이 짧고 유효 시간이 지나면 만료되어 인증이 불가능합니다.  


RefreshToken은 AccessToken의 유효 기간이 끝났을 때 새로운 AccessToken을 발급받을 수 있도록 도와줍니다.  







  
- **회원 역할 세분화**: 일반 회원, VIP 회원, 관리자
 ![image](https://github.com/user-attachments/assets/3b91c6d0-76bf-4a5a-a524-e4449b62ce8f)

  



- **ArgumentResolver와 Annotation**을 활용한 역할 기반 인가 처리
  ![image](https://github.com/user-attachments/assets/3f47c45f-3898-472d-a322-584362f6a848)

ArgumentResolver와 Annotation을 이용해 인가처리를 하였습니다.  



![image](https://github.com/user-attachments/assets/ac4ad6cb-602c-46a7-91f6-a2dbfd4eea30)  



@AuthUser라는 Annotation을 만든 후 ArgumentResolver에 인증 로직을 넣고 인가처리가 필요한 메소드에 다음과 같이 Annotation을 달아줘서 항상 인증 과정을 거치게됩니다.



![image](https://github.com/user-attachments/assets/aea3fbdc-cb6a-4056-938d-fb5a96ab9296)



번외로 운영자의 경우는 @Admin Annotation을 만들어서 운영자 여부 인증을 거치게 됩니다.



### 2. 상품 관리

- 상품 등록, 수정, 삭제 기능
- 
- **Parent ID**를 활용한 다중 카테고리 설정

  =====

### 3. 문의 게시판

- 문의 등록 및 답변 기능
- ![image](https://github.com/user-attachments/assets/c8116a3e-f194-4102-819c-4ed00a381303)

- ![image](https://github.com/user-attachments/assets/8fe0063f-3ba1-4082-a830-2d575f0b7568)


- 문의 상태 관리 (예: 답변 대기, 답변 완료)
![image](https://github.com/user-attachments/assets/2a76e981-8bd7-4c39-b0bd-b19eaa4dee6f)


유저는 자신의 주문에 대해서 문의 게시글을 올릴 수 있으며 운영자는 문의들에 대해 답변처리가 가능합니다.

### 4. 좋아요 및 포인트/쿠폰 시스템

- 상품에 대한 좋아요 기능
![image](https://github.com/user-attachments/assets/27c5f29f-72a8-40a3-a31b-09e71f128963)


좋아요버튼을 통해서 원하는 상품에 대해 찜 등록이 가능합니다.


- 쿠폰 발급 및 포인트 적립/사용 관리
- ![image](https://github.com/user-attachments/assets/f05c174d-367c-4aed-8e34-02d2851997df)

해당 주문에 대해서 쿠폰 및 리워드포인트 사용이 가능합니다.


![image](https://github.com/user-attachments/assets/a521976d-6426-4e0e-a72c-cbfc16ec27af)

쿠폰은 만료일, 최소주문금액, % 혹은 원 단위 설정이 가능합니다.

### 5. 결제 시스템

- 고객은 제품들을 장바구니에 담아서 주문 할 수 있습니다.
- \*\*토스(Toss)\*\*와의 결제 연동 (**가상 결제**)
- 
![image](https://github.com/user-attachments/assets/0eca0f12-1edd-46c7-ac2d-00dab8964117)
![11](https://github.com/user-attachments/assets/e5542f29-e33c-4b36-b4b2-1c8968e0df8a)
![제목 없음](https://github.com/user-attachments/assets/7a6d28d2-4e4a-4e20-b452-808dc9f17a5a)


  
- **낙관적 Lock**과 **Retry**를 이용한 동시성 결제 문제 해결

  ```java
  @Retryable(
            retryFor = {OptimisticLockException.class, StaleObjectStateException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Map<String, Object> confirmPaymentAndUpdate(OrderConfirmRequest orderConfirmRequest) {

        Order order = getOrderByExternalId(orderConfirmRequest.getExternalId());

        if(order.getStatus() != OrderStatus.결제전) {
            throw new InvalidOrderException("적절하지 않은 주문 요청입니다.");
        }

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
        if(coupon != null) {
            if(coupon.isUsed()) {throw new InsufficientRemainException(user.getNickname() +    "이미 사용된 쿠폰입니다.");}
            // 쿠폰 차감
            coupon.updateUsed();
        }

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


  ```


낙관적 락을 통해서 유저의 쿠폰 중복 사용, 리워드 포인트 한도 오류, 결제 된 주문 중복요청을 방지하고 상품의 재고에 대해 Race Condition으로 인한 재고보다 많은 주문요청을 방지하였습니다.
@Retryable Annotation을 통해서 낙관적 락의 버전 예외가 발생할시 최대 3번 재 요청을 하게 해주었습니다.
이렇게 되면 재 요청시에 재고가 없으면 구매가 불가능하지만 재고가 아직도 남아있다면 구매가 가능합니다.


![image](https://github.com/user-attachments/assets/0178b023-cc9d-4ca5-b2e2-15b6fdf5b12d)

다음과 같이 재고가 101개인 상품이 있을때 해당 상품을 2개씩 사는 주문 요청을 JMeter를 이용하여 동시성 테스트를 해보았습니다.

![image](https://github.com/user-attachments/assets/ea3f0bc9-d015-4d35-8ab2-f870435a64bf)

총 110회의 구매 요청을 하게됩니다.

![image](https://github.com/user-attachments/assets/1fb1575f-12a4-4877-a8e7-4d85314342c0)

정확히 51번째의 요청부터 품절된 상품으로 나오게됩니다.
50번째 요청까지는 2개씩 구매하므로 101개중에서 100개의 구매가 가능하기 때문입니다.


![image](https://github.com/user-attachments/assets/4d0356f0-175f-4041-b594-99a1d636f9ff)

정확히 한개의 재고가 남은 것을 알 수 있습니다.



### 6. 배치 작업

- **Spring Batch**를 활용한 다양한 작업:
  - 결제 취소 자동 처리
  - 회원 VIP 등급 자동 처리
  - 상품 판매 분석


### 7. 로그 추적기

![image](https://github.com/user-attachments/assets/24fb9520-a26e-4264-8145-b840971d9968)

Spring AOP를 이용하여 로그 추적기를 구현해보았습니다.
(Elastic Search?)


### 8. 파일 업로드 개선

- 상품 등록 시 이미지 저장을 **비동기 처리**하여 업로드 속도를 높이고 만약 파일 업로드 실패 시 어떡할지 롤백 처리 설계

### 9. 성능 최적화

N+1 문제가 발생 할 수 있는 코드는 Fetch Join을 이용하여 방지하였고
Pageable의 경우에는 Fetch Join시 limit문이 해결되지 않는 문제를 보였습니다.
그래서 @BatchSize 어노테이션을 통해서 동일한 where문에 대한 N+1 코드는 한번에 불러올 수 있도록 하였습니다.

그러나
![image](https://github.com/user-attachments/assets/2a69318e-ce0d-4b6b-ad58-b966f75048b1)
다음과 같이 300여개의 스레드를 통해 동시에 제품 페이지 요청을 할 때 매우 느린 응답속도를 보여서 문제를 확인해보니
JPA에서 제공하는 Page로 받을때 모든 데이터를 Count하고 페이징 처리를 하므로 300개 스레드의 요청에 대해서 임의로 넣어놓은 30만개의 product 데이터를 모두 카운팅하였기 때문입니다.
그래서 Page대신 Slice를 사용하여 해당 페이지처리에 필요한 product 데이터들만 받아와 페이징처리를 하였습니다.

![image](https://github.com/user-attachments/assets/9de1e390-87e8-455d-ab62-a1955319e282)


Page대신 Slice를 이용하니 시간이 현저히 줄어들었습니다.


등?

### 10. 배포

(다시 EC2 등록해야함)

---

