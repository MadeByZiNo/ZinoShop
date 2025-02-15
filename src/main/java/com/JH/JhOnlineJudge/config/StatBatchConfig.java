package com.JH.JhOnlineJudge.config;

import com.JH.JhOnlineJudge.user.admin.statistic.OrderProductStatDto;
import com.JH.JhOnlineJudge.user.admin.statistic.ProductStat;
import com.JH.JhOnlineJudge.user.admin.statistic.ProductStatJpaRepository;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import com.JH.JhOnlineJudge.order.repository.OrderJpaRepository;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.JobSynchronizationManager;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StatBatchConfig {

    private final JobRepository jobRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final ProductStatJpaRepository productStatJpaRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
     public Job updateStatJob(Step createStatStep, Step updateStatStep) {
         return new JobBuilder("updateStatJob",jobRepository)
                 .listener(jobExecutionListener())
                 .start(createStatStep)
                 .next(updateStatStep)
                 .build();
     }

    @Bean
    public Step createStatStep(ItemReader<Product> productReader,
                         ItemProcessor<Product, ProductStat> statCreateProcessor,
                               @Qualifier("statCreateWriter") ItemWriter<ProductStat> statCreateWriter) {
    return new StepBuilder("createStatStep",jobRepository)
           .<Product, ProductStat>chunk(10, platformTransactionManager)
           .reader(productReader)
           .processor(statCreateProcessor(null))
           .writer(statCreateWriter)
           .allowStartIfComplete(true)
           .build();
    }

    @Bean
        public RepositoryItemReader<Product> productReader() {
            return new RepositoryItemReaderBuilder<Product>()
                    .name("productReader")
                    .pageSize(10)
                    .methodName("findAll")
                    .repository(productJpaRepository)
                    .sorts(Map.of("id", Sort.Direction.ASC))
                    .build();
        }

      @Bean
      @StepScope
      public ItemProcessor<Product, ProductStat> statCreateProcessor(@Value("#{jobParameters[date]}") LocalDate date) {
          return product -> {
              ProductStat createdStat = ProductStat.of(date, product);
              return createdStat;
          };
      }


    @Bean
    public RepositoryItemWriter<ProductStat> statCreateWriter() {
        return new RepositoryItemWriterBuilder<ProductStat>()
                .repository(productStatJpaRepository)
                .methodName("save")
                .build();
    }



    @Bean
    public Step updateStatStep(ItemReader<ProductStat> productStatReader,
                            ItemProcessor<ProductStat, ProductStat> statProcessor,
                               @Qualifier("statUpdateWriter") ItemWriter<ProductStat> statWriter) {
      return new StepBuilder("updateStatStep",jobRepository)
              .<ProductStat, ProductStat>chunk(10, platformTransactionManager)
              .reader(productStatReader)
              .processor(statProcessor)
              .writer(statWriter)
              .allowStartIfComplete(true)
              .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<ProductStat> productStatReader(@Value("#{jobParameters[date]}") LocalDate date) {
      return new RepositoryItemReaderBuilder<ProductStat>()
              .name("productStatReader")
              .pageSize(10)
              .methodName("findAllByDate")
              .arguments(date)
              .repository(productStatJpaRepository)
              .sorts(Map.of("id", Sort.Direction.ASC))
              .build();
    }

    @Bean
    public ItemProcessor<ProductStat, ProductStat> StatUpdateProcessor() {
    return productStat -> {

        Map<Long, List<OrderProductStatDto>> orderProductMap = (Map<Long, List<OrderProductStatDto>>)
                JobSynchronizationManager.getContext().getJobExecution().getExecutionContext().get("orderProductMap");

        List<OrderProductStatDto> orderProducts = orderProductMap.get(productStat.getProduct().getId());

        int totalQuantity = 0;
        int totalPrice = 0;

        if (orderProducts != null) {
            totalQuantity = orderProducts.stream().mapToInt(OrderProductStatDto::getQuantity).sum();
            totalPrice = orderProducts.stream()
                                      .mapToInt(op -> op.getQuantity() * op.getPrice())
                                      .sum();
        }

        productStat.update(totalQuantity, totalPrice);
        return productStat;
    };
    }

    @Bean
    public RepositoryItemWriter<ProductStat> statUpdateWriter() {
      return new RepositoryItemWriterBuilder<ProductStat>()
              .repository(productStatJpaRepository)
              .methodName("save")
              .build();
    }

    /**
     *  1. 오늘 구매확정인 모든 주문(Order)들을 불러온다
     *  2. 주문들에 담긴 모든 orderProduct들을 순회한다.
     *  3. 순회하는 과정에서 해당 product.id를 key로 hashmap을 통해 해당 orderProduct를 넣어준다.
     *  4. 이제 processor에서는 해당 product의 id를 이용해 findAll로 개수와 가격들을 받아주면 된다.
     */
    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                LocalDate date = jobExecution.getJobParameters().getLocalDate("date");
                List<Order> orders = orderJpaRepository.findAllByDeliveredAtAndStatus(
                        date.atStartOfDay(), date.atTime(LocalTime.MAX), OrderStatus.구매확정);
                log.info("ordres = {}",orders.toString());
                Map<Long, List<OrderProductStatDto>> orderProductMap = orders.stream()
                        .flatMap(order -> order.getOrderProducts().stream())
                        .map(OrderProductStatDto::from)
                        .collect(Collectors.groupingBy(orderProduct -> orderProduct.getProductId()));

                jobExecution.getExecutionContext().put("orderProductMap", orderProductMap);

            }
        };
    }
}
