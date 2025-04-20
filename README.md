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


![erd](https://github.com/user-attachments/assets/fd7d1f9b-37fc-49e0-9d13-a4dcbfbdd01e)


<br>


---


### 아키텍처 구조


<br>


![설계구조](https://github.com/user-attachments/assets/6733435d-b05c-4bae-a9da-47159541b56e)


<br>


Github Action을 통해서 개발자가 운영 Branch에 push를 하면 감지를 해서 EC2에서 pull을 하고 빌드해서 JAR파일을 생성 후 Dockerfile을 통해서  


프로젝트 서버 Docker 이미지를 생성하고 컨테이너에서 실행시키게 해놓아서 자동배포를 구현하였습니다.


### 현재는 비용 문제로 인해 로컬에서 돌리는중입니다.


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


![image](https://github.com/user-attachments/assets/0d023fcb-7808-496a-8aea-395e26993739)



<br>


---


<br>


## 📌 주요 기능


<br>


### 1. 회원 가입 및 로그인


<br>



![ezgif-678f742c6d0330](https://github.com/user-attachments/assets/1242ce87-590a-42bf-81fe-486e3c6d0363)



<br>


 **일반 회원가입 및 네이버 및 카카오를 통한 회원가입이 가능합니다.**



<br>




### 2. 상품 조회


<br>



![1](https://github.com/user-attachments/assets/21d1074b-453b-40d5-ba49-1381405723d6)

<br>

무한 스크롤을 통한 상품 조회




<br>



![2](https://github.com/user-attachments/assets/f7211a94-513e-4128-92b7-46e01424bec6)

<br>

정렬 순서 및 가격 제한 필터





<br>



![3](https://github.com/user-attachments/assets/5f3b6064-b45a-412b-ba44-391388712836)

<br>

상품 직접 검색 및 자동완성 기능



<br>




### 3. 좋아요(찜) 기능


<br>


![heart](https://github.com/user-attachments/assets/9fd43910-f6e8-4d1b-865c-5e9414b14fad)



<br>


좋아요 버튼을 통해 원하는 상품에 대해 찜 등록이 가능합니다.


찜 화면에서 자신이 좋아요를 누른 상품들을 볼 수 있습니다.



<br>



### 4. 장바구니 담기


<br>


![cart](https://github.com/user-attachments/assets/87422fbf-7f26-4e23-a063-9af08c65fae7)



<br>


상품들을 장바구니에 담아 삭제하거나 수량을 수정 할 수 있습니다.



<br>



### 5. 상품 주문


<br>


![1](https://github.com/user-attachments/assets/e162d122-98a2-4619-a297-502078f3d073)
![2](https://github.com/user-attachments/assets/7cb0ef9c-b038-40c0-9840-b6898f4677a2)


<br>


### **토스(Toss)**와의 결제 연동된 시스템으로 상품을 구매 할 수 있습니다.(**가상 결제**)


**- 쿠폰 및 포인트 적립/사용 관리**

주문에 대해서 **쿠폰** 및 **리워드 포인트** 사용이 가능합니다.  
쿠폰은 만료일, 최소주문금액, % 혹은 원 단위 설정이 가능합니다.


<br>



### 6. 리뷰 작성


<br>



![image](https://github.com/user-attachments/assets/947844de-f547-4d57-bad8-ad1219506f26)



<br>



구매한 상품들에 대해 리뷰를 작성 할 수 있으며 중복작성은 불가능합니다.


자신이 단 리뷰들을 확인 할 수 있으며 삭제가 가능합니다.



<br>



### 7. 문의 게시판



<br>



![1](https://github.com/user-attachments/assets/26268ba0-ae8b-408f-9536-c83c57b0498a)
![2](https://github.com/user-attachments/assets/3c0750b9-95ab-4be3-a8e9-e60bc0586523)



<br>


주문에 대해서 문의 글을 올릴 수 있으며 운영진은 문의에 대해 답변이 가능합니다.


사용자에게는 문의 답변에 대해 알림이 갑니다.



<br>




### 8. 상품 관리


<br>

![image](https://github.com/user-attachments/assets/ec468b57-9f9d-45df-9eae-34def3e7ee4c)



<br>


 **상품 등록 , 수정 및 삭제**



<br>



![image](https://github.com/user-attachments/assets/3141d7cd-21aa-425d-8dbd-6c4937e0822c)


<br>


![image](https://github.com/user-attachments/assets/a9a2c022-7d43-4e27-8af9-5c197c4956c7)



<br>


### 99. 배송 관리


<br>


![image](https://github.com/user-attachments/assets/3ca1a04c-3df0-4abf-a15a-de9d5bfc8272)



<br>



관리자는 주문에 대해 배송 상태를 보거나 임의로 설정이 가능합니다.



<br>




### 10. 상품 판매 분석 


<br>



![1](https://github.com/user-attachments/assets/a68aa403-fac5-4d29-8b8a-3ea8398fddfd)
![2](https://github.com/user-attachments/assets/7f2f7049-ec1f-478b-b95b-1dd947a15044)



<br>


상품 자체의 판매 내역이나 해당 일, 월, 년의 가장 많이 팔린 상품 및 가장 많은 수익을 낸 상품 Top30을 그래프로 확인이 가능합니다.



<br>



### **서버 내부 구현**




<br>





### **회원 및 인증 관리**



<br>



- **JWT**를 이용한 로그인 및 세션 관리




![JWT](https://github.com/user-attachments/assets/e1b052fc-a7ea-470e-beeb-4abf705aad14)



<br>



사용자가 로그인하면 서버는 JWT 형태의 **AccessToken**과 **RefreshToken**을 발급합니다.  
이 두 토큰은 클라이언트의 **쿠키**에 저장되며 탈취 위험성을 위해 **유효 시간을 설정**합니다.



<br>

- **AccessToken**은 인증하는 데 사용되며 유효 시간이 짧고, 유효 시간이 지나면 만료되어 인증이 불가능합니다.  
- **RefreshToken**은 AccessToken의 유효 기간이 끝났을 때 새로운 AccessToken을 발급받을 수 있도록 도와줍니다.



<br>
  
![image](https://github.com/user-attachments/assets/a030d8ba-4ee1-40ff-8430-5ee20f3e4171)
    
       
 <br>



 
- **ArgumentResolver와 Annotation**을 활용한 역할 기반 인가 처리


![image](https://github.com/user-attachments/assets/f485ca3a-bd76-4678-8d24-171aaa4067cd)
![AuthUser Annotation](https://github.com/user-attachments/assets/ac4ad6cb-602c-46a7-91f6-a2dbfd4eea30)




`@AuthUser`라는 Annotation을 만든 후 `ArgumentResolver`에 인증 로직을 넣고, 인가 처리가 필요한 메소드에 해당 Annotation을 추가하여 인증과정을 거칩니다.  

운영자의 경우, `@Admin` Annotation을 만들어서 운영자 여부 인증을 거치게 합니다.  


![image](https://github.com/user-attachments/assets/576137fa-ea7b-49ee-bba1-59300d640113)



<br>




### ✅ Redis를 활용한 인증 최적화 이유 및 장점

기존에는 페이지 전환 시마다 사용자 인증 정보를 확인하기 위해  **DB를 매번 조회**했기 때문에  
트래픽이 많아질수록 **성능 저하**가 발생할 우려가 있었습니다.

<br>



이를 해결하기 위해 In-Memory 데이터 저장소인 **Redis**를 도입했습니다.



<br>



- 사용자가 **최초 로그인 시**, 발급된 **AccessToken**과 **RefreshToken**에  
  `userId`, `username`, `nickname`, `role` 등 핵심 유저 정보를 담아  
  **Redis에 저장**합니다.


<br>



- 이후 인증이나 인가가 필요할 때마다  
  **DB 조회 없이 Redis에서 바로 유저 정보를 가져올 수 있어**  
  조회 속도가 매우 빠르고 서버 부하를 최소화할 수 있습니다.



<br>



![스크린샷 2025-03-19 165902](https://github.com/user-attachments/assets/57c009b4-5788-4799-a883-f302e2ed1819)



<br>




### Oauth2.0을을 이용한 회원 가입 및 로그인###

![image](https://github.com/user-attachments/assets/97c9c81e-ee81-4a0e-b92e-c3b17def383f)



네이버 및 카카오 OAUTH2.0 로그인을 구현하였습니다.
외에 일반 사이트 자체 로그인도 가능합니다.
추후 구글 등 사이트들을 추가하기 쉽게 추상화 및 팩토리 구조로 만들었습니다.

---



<br>




## **낙관적 락을 통한 동시성 문제 해결**


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

### **Spring Batch 기반 자동화 작업**

<br>

`Spring Batch`를 활용하여 주기적으로 이루어지는 반복적인 작업을 자동화하고,  
시스템 유지 관리를 효율적으로 수행할 수 있도록 배치 작업을 구성했습니다.

<br>


- 🌟 **회원 VIP 등급 자동 처리**  
  매달 말일, 회원들의 이번 달 구매 금액을 기준으로 VIP 등급을 자동으로 부여하거나 해제하는 배치 기능을 구현했습니다.
  10만 원 이상 구매한 회원은 VIP 고객님으로, 그 외는 일반 고객님으로 변경됩니다.

<br>

![image](https://github.com/user-attachments/assets/43fe64dd-fad6-4d79-861f-08b759a387a6)


<br>


  기존의 Processor를 그대로 이용하면 process 과정에서 가격을 불러오는 DB작업을 유저 수 만큼 호출하므로 비효율적이라 판단했습니다.
  
  그래서 Reader와 Processor를 커스터마이징해주었습니다.
  
  Reader에서는 Chunk 주기마다 한번에 Redis에 id들을 캐싱해주었으며
  
  Processor에서는 Chunk 주기마다 한번에 Redis의 id를 가져와 가격을 벌크쿼리를 통해 가져온 후에 등급을 처리하였습니다.
  
  500만건의 DB 호출에서 chunk의 크기인 1000배 만큼 호출 수를 줄여서 최적화해주었습니다.


<br>


  **redis를 이용해서 캐싱한 이유**

  executionContext를 통해서 Step단위에서 Reader와 Processor가 Id들을 공유하는 것을 생각했으나 2500kb까지밖에 담지 못하는 이유로 Redis를 이용하였습니다.


<br>



- 📊 **상품 판매량 분석**  
  매일매일 상품들의 판매 이력을 집계하여 별도 통계 테이블에 저장합니다.  
  이를 기반으로 관리자 창에서 많이 팔린 상품과 특정 상품의 판매 량 및 판매 금액의 시각화가 가능합니다.


<br>


---


<br>


### **로그 추적기**


<br>


![image](https://github.com/user-attachments/assets/c69913b2-74f6-4257-8b74-28bfac653982)

![image](https://github.com/user-attachments/assets/450e0134-36f8-4c9e-b466-105da5e0c849)


<br>


Spring AOP와 Logback MDC를 이용해 요청 단위로 로그를 추적할 수 있도록 구현했습니다.
주 관심사인 Controller, Service, Repository 계층에 AOP를 적용해 메서드의 실행 흐름과 처리 시간, 예외 발생 지점을 추적할 수 있도록 했습니다.

요청마다 고유한 traceId가 생성되며, MDC를 통해 멀티 스레드 환경에서도 로그가 섞이지 않도록 관리됩니다.
로그는 콘솔과 별도의 로그 파일로 함께 기록되며, 파일은 운영 중 이슈 분석이나 디버깅 시 활용할 수 있도록 저장됩니다.

<br>


---


<br>



### **비동기 이미지 업로드 & 이메일 전송**

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



-### **N+1 문제 해결**: Fetch Join을 사용하여 N+1 문제를 방지했습니다.


- Pageable을 사용한 경우, Fetch Join 시 **limit**문이 적용되지 않는 문제를 보았습니다.  
그래서 @BatchSize 어노테이션을 사용하여 동일한 where문에 대한 N+1 문제를 한번에 불러올 수 있도록 최적화했습니다.


<br>


### <span style="color:gray;">Pagable의 성능 문제</span>  (이 부분은 쇼핑몰 특성상 Slice를 이용한 페이징에서 무한스크롤로 대체되었습니다.) ###



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



<br>



#### ✅ 상품별 통계 TOP30 조회 속도 비교


<br>


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


<br>



### 검색 속도 개선 ###



<br>

기존에는 단순한 RDBMS를 사용하여 쿼리문으로 상품을 가져올 때 LIMIT을 걸어도 데이터가 적을 경우 해당 LIMIT만큼 채우지 못하고, 3천만개 데이터에 대해 풀 스캔이 발생하여 검색 속도가 느려지는 문제가 있었습니다. 

특히, 상품을 찾을 때는 무조건 이름을 기준으로 검색하고, 추가 조건(카테고리 이름으로 검색, 설명으로 검색)이 있을 경우 AND문을 걸어줘야하므로 쿼리가 복잡해지고 무거워질 것이라고 생각했습니다.


<br>


**Elasticsearch 활용**


<br>


이를 해결하기 위해 최신 검색 기술인 Elasticsearch를 도입했습니다.


<br>


ElasticSearch는 강력한 검색기능 외에도 집계, 분석, 시각화 등 많은 방면으로 쓰일 수 있어서 좋은 대안이라고 생각했습니다.


<br>



**Copy_to를 활용한 형태소 분석 및 데이터 처리:**

상품 이름과 카테고리를 모두 keywordtext로 넣어서 하나의 필드에서 바로 쉽게 검색할 수 있도록 했습니다. 이 과정에서 Copy_to 기능을 활용하여 여러 필드를 하나로 묶어 놓음으로써 복잡한 JOIN 없이 빠르게 검색할 수 있도록 최적화했습니다.

<br>


이를 통해, 검색 성능은 물론이고, 데이터 조회 속도도 대폭 향상되었습니다. 또 조건이 추가되어도 성능 저하 없이 빠르고 효율적인 검색이 가능해졌습니다.

<br>



**Elasticsearch 활용해서 얻은 이점**

<br>


**ElasticSearch의 역색인을 통한 매우 빠른 검색기능을 통해서 상품검색 자동완성 기능을 구현이 가능하였습니다.**


<br>



**검색 부분에서 매우 빠른 속도 향상을 보여주었습니다.**


3천만개의 데이터를 임의로 삽입하고 http://localhost:8080/product/list?category_id=1&minPrice=0&maxPrice=60000&keyword=%EA%B2%80%EC%A0%95&sort=price_asc 다음과 같이 복합적인 조건이 있는 검색을 요청해보았습니다.


<br>


ElasticSearch 도입 전 DB에 쿼리를 통해서 데이터를 가져올때는 


![image](https://github.com/user-attachments/assets/56a1b83e-07c5-4c11-89f2-c87618e2e9e4)


다음과 같이 14초나 소모되었습니다.


<br>


ElasticSearch를 도입할때 일관성 보장이 낮고 트랜잭이 없으므로 실제 RDBMS처럼 사용 할 수는 없다고 생각했습니다.


<br>

그래서 product 인덱스안에는 가격 이름 id정도의 정보만 저장하게끔 해놓고 해당 조건에 맞는 상품들의 id들을 불러와서


<br>

In절을 통해서 매우 빠르게 데이터를 가져오게끔 구현했습니다.


<br>


![image](https://github.com/user-attachments/assets/4a9c859c-95ee-43d6-af51-b4374303a7c8)




