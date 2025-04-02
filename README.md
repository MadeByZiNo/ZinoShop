# 🛒 ZinoShop 프로젝트


**Spring, Spring Boot, JPA, MySQL**를 기반으로 한 **서버 사이드 렌더링(SSR)** 쇼핑몰 프로젝트. SPRING MVC를 이용하여 JWT를 활용한 인증 및 세션 관리, 상품 관리, 결제 연동 등 다양한 기능을 포함하여 실제 운영 중인 쇼핑몰 시스템의 구조를 예상하여 개발해보았습니다.


---


### 개발 환경


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


### 프론트엔드 (프론트는 주로 ChatGPT의 도움을 받았습니다)


- **HTML**


- **JavaScript**


- **Thymeleaf**


---


### ERD


<br>


![ERD](https://github.com/user-attachments/assets/918f56f4-449f-454b-a552-4e14f935766a)


<br>


---


### 아키텍처 구조


<br>


![설계구조](https://github.com/user-attachments/assets/6733435d-b05c-4bae-a9da-47159541b56e)


<br>


Github Action을 통해서 개발자가 운영 Branch에 push를 하면 감지를 해서 EC2에서 pull을 하고 빌드해서 JAR파일을 생성 후 Dockerfile을 통해서  


프로젝트 서버 Docker 이미지를 생성하고 컨테이너에서 실행시키게 해놓아서 자동배포를 구현하였습니다.


<br>


---


### yml 관리


<br>


![image](https://github.com/user-attachments/assets/b6c996f1-8802-4e1d-b543-fdeb225b5015)


<br>


로컬 개발과 실제 운영 환경은 다를 수 밖에 없습니다. (DB의 URL, JPA 초기화설정 등)


그래서 application.yml을 개발 환경용인 application-local.yml과 application-prod.yml로 나눠서 사용하였습니다.


특히 시크릿키 등의 환경변수의 경우에는 보안상 로컬에서 그대로 옮길 수 없으므로 Github Secrets을 이용하여 저장해주었고 EC2 배포과정에서 환경변수들을 담아서 보내줍니다.


<br>


---


### 메인 화면


<br>


![image](https://github.com/user-attachments/assets/916b1382-a895-4067-a661-c9a912edc70c)


<br>


---


<br>


## 📌 주요 기능


<br>


### 2. 상품 관리


<br>


- 상품 등록, 수정, 삭제 기능


- **Parent ID**를 활용한 다중 카테고리 설정


<br>


---


<br>


### 3. 문의 게시판


<br>


- 문의 등록(유저)


<br>


![Inquiry](https://github.com/user-attachments/assets/c8116a3e-f194-4102-819c-4ed00a381303)


<br>


- 문의 답변(운영자)


<br>


![Inquiry Status](https://github.com/user-attachments/assets/2a76e981-8bd7-4c39-b0bd-b19eaa4dee6f)


<br>


---


<br>


### 4. 좋아요 및 포인트/쿠폰 시스템


<br>


![Like](https://github.com/user-attachments/assets/27c5f29f-72a8-40a3-a31b-09e71f128963)


<br>


좋아요 버튼을 통해 원하는 상품에 대해 찜 등록이 가능합니다.


찜 화면에서 자신이 좋아요를 누른 상품들을 볼 수 있습니다.


<br>


**- 쿠폰 발급 및 포인트 적립/사용 관리**


<br>


![Coupon Usage](https://github.com/user-attachments/assets/a521976d-6426-4e0e-a72c-cbfc16ec27af)


<br>


주문에 대해서 **쿠폰** 및 **리워드 포인트** 사용이 가능합니다.  
쿠폰은 만료일, 최소주문금액, % 혹은 원 단위 설정이 가능합니다.


<br>


---


<br>


### 5. 결제 시스템


<br>


![Coupon](https://github.com/user-attachments/assets/f05c174d-367c-4aed-8e34-02d2851997df)


<br>


고객은 제품들을 장바구니에 담아서 주문 할 수 있습니다.


<br>


### **토스(Toss)**와의 결제 연동 (**가상 결제**)


<br>


![Payment](https://github.com/user-attachments/assets/0eca0f12-1edd-46c7-ac2d-00dab8964117)


<br>


![Payment Confirmation](https://github.com/user-attachments/assets/e5542f29-e33c-4b36-b4b2-1c8968e0df8a)


<br>


---


<br>


### **낙관적 락을 통한 동시성 문제 해결**


<br>


낙관적 락을 사용하여 **유저의 쿠폰 중복 사용**, **리워드 포인트 한도 오류**, **결제된 주문 중복 요청**을 방지하였고,  
상품의 재고에 대해 **Race Condition**으로 인한 재고보다 많은 주문 요청을 방지하였습니다.


<br>


`@Retryable` 어노테이션을 사용하여 **낙관적 락의 버전 예외**가 발생할 경우  
최대 **3번 재요청**을 하게 해주었습니다.  
이로 인해 재요청 시 **재고가 없으면 구매가 불가능**, **재고가 남아있으면 구매 가능**하게 됩니다.


<br>


### **JMeter 동시성 테스트**


<br>


다음과 같이 재고가 **101개**인 상품에 대해 **2개씩 구매**하는 주문 요청을  
**JMeter**를 이용해 동시성 테스트를 진행했습니다.


<br>


![JMeter Testing](https://github.com/user-attachments/assets/0178b023-cc9d-4ca5-b2e2-15b6fdf5b12d)


<br>


**총 110회의 구매 요청**을 했으며, 정확히 **51번째 요청부터 품절된 상품**으로 나타났습니다.


- **50번째 요청까지는 2개씩 구매**하므로 **101개 중 100개가 판매**됩니다.


- **51번째 요청부터는 거절 처리**되었습니다. (재고보다 많은 물량 구매요청)


<br>


![Remaining Stock](https://github.com/user-attachments/assets/4d0356f0-175f-4041-b594-99a1d636f9ff)


<br>


정확히 **한 개의 재고**가 남은 것을 알 수 있습니다.


<br>


---


<br>

### **8. Spring Batch 기반 자동화 작업**

<br>

`Spring Batch`를 활용하여 주기적으로 이루어지는 반복적인 작업을 자동화하고,  
시스템 유지 관리를 효율적으로 수행할 수 있도록 배치 작업을 구성했습니다.

<br>

- **결제 취소 자동 처리**  
  주문 후 일정 시간 내 결제가 이루어지지 않은 주문을  
  배치 스케줄러가 자동으로 취소 처리합니다.  
  (※ 실제 PG 결제는 구현되어 있지 않지만, 취소 상태 전환 로직 구현)

<br>

- 🌟 **회원 VIP 등급 자동 처리**  
  회원의 등급을 달의 마지막 날에 이번 달의 구매량을 분석하여 일반 → VIP로 자동 승격시키고
  부족한 유저는 →  일반으로 강등시켰습니다.

<br>

- 📊 **상품 판매량 분석**  
  매일매일 상품들의 판매 이력을 집계하여 별도 통계 테이블에 저장합니다.  
  이를 기반으로 관리자 창에서 많이 팔린 상품과 특정 상품의 판매 량 및 판매 금액의 시각화가 가능합니다.


<br>

---


<br>


### **7. 로그 추적기**


<br>


![image](https://github.com/user-attachments/assets/c69913b2-74f6-4257-8b74-28bfac653982)



<br>


Spring AOP와 Logback MDC를 이용해 요청 단위로 로그를 추적할 수 있도록 구현했습니다.
주 관심사인 Controller, Service, Repository 계층에 AOP를 적용해 메서드의 실행 흐름과 처리 시간, 예외 발생 지점을 추적할 수 있도록 했습니다.

요청마다 고유한 traceId가 생성되며, MDC를 통해 멀티 스레드 환경에서도 로그가 섞이지 않도록 관리됩니다.
로그는 콘솔과 별도의 로그 파일로 함께 기록되며, 파일은 운영 중 이슈 분석이나 디버깅 시 활용할 수 있도록 저장됩니다.

<br>


---


<br>



### **8. 비동기 이미지 업로드 & 이메일 전송**

<br>

Spring의 `@Async`와 `ThreadPoolTaskExecutor`를 사용하여  
시간이 오래 걸릴 수 있는 작업을 비동기로 처리했습니다.  

<br>

- **상품 이미지 업로드**  
  Amazon S3를 통해 여러 장 이미지를 동시에 업로드할 경우 사용자 응답 속도가 저하될 수 있습니다.
  10개의 이미지를 업로드 한 후에 상품 추가 버튼을 누르면 4초가량 페이지가 멈춘듯한 느낌을 주고 저장완료 처리가 되었기에    
  이를 해결하기 위해 각 이미지 업로드를 비동기로 실행하여 바로 업로드 된 것 처럼 보이게 해서 **빠른 사용자 응답**을 주어 UX를 개선했습니다.  

<br>

- **이메일 전송**  
  회원가입 인증 등 메일 발송도 외부 SMTP와의 통신으로 인해 지연 요소가 되므로 비동기처리를 통해 메일 전송 로직을 분리하고,  
  메일 발송 중에도 사용자에게는 빠르게 응답을 돌려주는 구조를 구현했습니다.


<br>

### **성능 최적화**


<br>


- **N+1 문제 해결**: Fetch Join을 사용하여 N+1 문제를 방지했습니다.


- Pageable을 사용한 경우, Fetch Join 시 **limit**문이 해결되지 않는 문제를 보았습니다.  
그래서 @BatchSize 어노테이션을 사용하여 동일한 where문에 대한 N+1 문제를 한번에 불러올 수 있도록 최적화했습니다.


<br>


##### **Pagable의 성능 문제**


<br>


![Performance Issue](https://github.com/user-attachments/assets/2a69318e-ce0d-4b6b-ad58-b966f75048b1)


<br>


300여 개의 스레드를 통해 동시에 제품 페이지 요청을 할 때, 매우 느린 응답 속도가 발생하는 문제를 확인했습니다.


많은 량의 데이터를 테스트하기 위해 임의로 30만개의 product를 INSERT 하였습니다.


JPA에서 제공하는 **Page**로 데이터를 받을 때 모든 PRODUCT 데이터를 **Count**하고 페이징 처리를 하므로,  
300개 스레드의 요청에 대해 **30만 개의 product 데이터**를 모두 카운팅하고 있었습니다.


이를 해결하기 위해 **Page 대신 Slice**를 사용하여, 해당 페이지 처리에 필요한 데이터만 받아와 페이징 처리를 하였습니다.


<br>


![Slice Performance](https://github.com/user-attachments/assets/9de1e390-87e8-455d-ab62-a1955319e282)


<br>


**Slice**를 이용하니 응답속도가 최소 1/10가량 줄면서 성능이 현저히 향상되었습니다.


<br>


### 인덱스를 이용한 성능 최적화

720만 건 이상의 데이터를 가진 `product_stat` 테이블에서 상품별 통계를 조회하는 과정에서 성능 병목이 발생했습니다.  
이를 개선하기 위해 **인덱스를 추가 적용**하여 조회 속도를 대폭 개선하였습니다.

<br>

![테이블 구성](https://github.com/user-attachments/assets/9a1e17b4-d9e9-4471-a900-47c840b7da43)  
*이미지: product_stat 테이블 구성 (상품 ID, 날짜 기준 통계)*

<br>

통계 조회 시 초기에는 **약 2.1초**가 소요되었으며, 데이터가 많아질수록 더욱 느려질 것이므로 성능 저하가 우려되었습니다.

<br>

![쿼리 속도 전](https://github.com/user-attachments/assets/9e623928-f045-4252-918a-eead0d61fa38)  
*이미지: 인덱스 적용 전 통계 조회 쿼리 소요 시간*

<br>

JPQL 쿼리에서 `product_id`와 `date` 순으로 조회가 이루어졌기 때문에  
해당 컬럼 조합에 대해 복합 인덱스를 생성하였습니다.

<br>

![인덱스 생성 후 속도](https://github.com/user-attachments/assets/bacc071d-846a-44ab-8f30-7ef0843f4f79)  
*이미지: 인덱스 적용 후 통계 쿼리 속도 개선 결과*

<br>

---

#### ✅ 상품별 통계 TOP30 조회 속도 비교

TOP30 통계를 일/월/년 단위로 각각 조회하였고, 인덱스 적용 전후 성능 차이를 확인해보았습니다.

<br>

![일별 TOP30 - Before](https://github.com/user-attachments/assets/26862e70-f1c7-4230-90fc-0f56433bd1cf)  
*이미지: 인덱스 적용 전 일별 TOP30 조회 속도*

<br>

![월별 TOP30 - Before](https://github.com/user-attachments/assets/279e1442-5d4c-422c-8598-d40aa34aa458)  
*이미지: 인덱스 적용 전 월별 TOP30 조회 속도*

<br>

![연별 TOP30 - Before](https://github.com/user-attachments/assets/3534c939-21c7-4047-928d-8c3b648d1c8d)  
*이미지: 인덱스 적용 전 연별 TOP30 조회 속도 (최대 8초)*

<br>

전과는 달리 `date`, `product_id` 순서를 기준으로 인덱스를 생성하여 최적화하였습니다.

<br>

![일별 TOP30 - After](https://github.com/user-attachments/assets/de080bc2-93c9-4e77-9901-957deece8d05)  
*이미지: 인덱스 적용 후 일별 TOP30 속도 개선*

<br>

![월별 TOP30 - After](https://github.com/user-attachments/assets/6d1c0750-1f17-45ff-882d-e8d96cc7f3a9)  
*이미지: 인덱스 적용 후 월별 TOP30 속도 개선*
