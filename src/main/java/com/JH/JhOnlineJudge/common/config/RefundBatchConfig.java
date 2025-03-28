package com.JH.JhOnlineJudge.common.config;

import com.JH.JhOnlineJudge.domain.order.exception.RefundRequestException;
import com.JH.JhOnlineJudge.domain.order.entity.Order;
import com.JH.JhOnlineJudge.domain.order.entity.OrderStatus;
import com.JH.JhOnlineJudge.domain.order.repository.OrderJpaRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RefundBatchConfig {

    @Value("${toss.secret-key}")
    private String SECRET_KEY;

    private final JobRepository jobRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final RestTemplate restTemplate;

    @Bean
     public Job refundRequestJob(Step refundRequestStep) {
         return new JobBuilder("refundRequestStep",jobRepository)
                 .start(refundRequestStep)
                 .build();
     }

    @Bean
    public Step refundRequestStep(ItemReader<Order> OrderReader,
                         ItemProcessor<Order, Order> refundRequestProcessor,
                         ItemWriter<Order> orderWriter) {
    return new StepBuilder("refundRequestStep",jobRepository)
           .<Order, Order>chunk(10, platformTransactionManager)
           .reader(OrderReader)
           .processor(refundRequestProcessor)
           .writer(orderWriter)
           .allowStartIfComplete(true)
            .faultTolerant()
            .skip(RefundRequestException.class)
            .listener(new SkipListener<Order, Order>() {
                @Override
                public void onSkipInProcess(Order item, Throwable t) {
                    log.error("Refund Request Error " +
                            "OrderId: {}  " +
                                    "PaymentKey: {}" +
                            "massage: {}"
                    ,item.getId(), item.getPaymentKey(), t.getMessage() );
                }
            })
           .build();
    }

    @Bean
    public RepositoryItemReader<Order> OrderReader() {
        return new RepositoryItemReaderBuilder<Order>()
                .name("OrderReader")
                .pageSize(10)
                .methodName("findAllByStatus")
                .repository(orderJpaRepository)
                .arguments(OrderStatus.환불신청)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Order, Order> refundRequestProcessor() {
      return order -> {
          HttpHeaders headers = new HttpHeaders();
          headers.setBasicAuth(SECRET_KEY, "");
          headers.setContentType(MediaType.APPLICATION_JSON);

          Map<String, Object> requestBody = Map.of(
                  "cancelReason", "환불 요청"
          );

          HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

          try{
              restTemplate.exchange(
                      "https://api.tosspayments.com/v1/payments/{paymentKey}/cancel",
                      HttpMethod.POST,
                      request,
                      String.class,
                      order.getPaymentKey()
              );
          } catch (RestClientResponseException ex) {
              String responseBody = ex.getResponseBodyAsString();

              ObjectMapper mapper = new ObjectMapper();
              Map<String, String> errorResponse = mapper.readValue(responseBody, new TypeReference<>() {});
              String errorCode = errorResponse.get("code");
              String errorMessage = errorResponse.get("message");

              log.error("Error Code: {}", errorCode);
              log.error("Error Message: {}", errorMessage);

              String message = "Error Code:  " + errorCode + "   Error Message:  " + errorMessage;
              throw new RefundRequestException(message);
          }

          order.updateStatus(OrderStatus.환불완료);
          return order;
      };
    }


    @Bean
    public RepositoryItemWriter<Order> orderWriter() {
        return new RepositoryItemWriterBuilder<Order>()
                .repository(orderJpaRepository)
                .methodName("save")
                .build();
    }

}
