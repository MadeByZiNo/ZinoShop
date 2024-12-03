package com.JH.JhOnlineJudge.config;

import com.JH.JhOnlineJudge.common.OrderProduct.OrderProduct;
import com.JH.JhOnlineJudge.common.statistic.ProductStat;
import com.JH.JhOnlineJudge.common.statistic.ProductStatJpaRepository;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import com.JH.JhOnlineJudge.order.repository.OrderJpaRepository;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.repository.ProductJpaRepository;
import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.domain.UserRole;
import com.JH.JhOnlineJudge.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.joda.time.DateTime;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class StatBatchConfig {


    private final JobRepository jobRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final ProductStatJpaRepository productStatJpaRepository;
    private final PlatformTransactionManager platformTransactionManager;


    @Bean
     public Job updateStatJob(Step createStatStep, Step updateStatStep) {
         return new JobBuilder("updateStatJob",jobRepository)
                 .start(createStatStep)
                 .next(updateStatStep)
                 .build();
     }

    @Bean
    public Step createStatStep(ItemReader<Product> productReader,
                         ItemProcessor<Product, ProductStat> statCreateProcessor,
                         ItemWriter<ProductStat> statCreateWriter) {
    return new StepBuilder("createStatStep",jobRepository)
           .<Product, ProductStat>chunk(10, platformTransactionManager)
           .reader(productReader)
           .processor(statCreateProcessor)
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
      public ItemProcessor<Product, ProductStat> statCreateProcessor() {
          return product -> {
              ProductStat createdStat = ProductStat.of(LocalDate.now(), product);
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
                            ItemWriter<ProductStat> statWriter) {
      return new StepBuilder("updateStatStep",jobRepository)
              .<ProductStat, ProductStat>chunk(10, platformTransactionManager)
              .reader(productStatReader)
              .processor(statProcessor)
              .writer(statWriter)
              .allowStartIfComplete(true)
              .build();
    }

    @Bean
    public RepositoryItemReader<Order> productStatReader() {
      return new RepositoryItemReaderBuilder<Order>()
              .name("productStatReader")
              .pageSize(10)
              .methodName("findByDate")
              .arguments(LocalDate.now())
              .repository(productStatJpaRepository)
              .sorts(Map.of("id", Sort.Direction.ASC))
              .build();
    }

    @Bean
    public ItemProcessor<ProductStat, ProductStat> orderStatProcessor() {
    return productStat -> {

        Map<Long, List<OrderProduct>> orderProductMap = (Map<Long, List<OrderProduct>>)

                       StepContext.getExecutionContext().get("orderProductMap");
        List<Order> orders = orderJpaRepository.findAllByDeliveredAtAndStatus(LocalDate.now().atStartOfDay(),
                LocalDate.now().atTime(LocalTime.MAX),
                OrderStatus.구매확정);

        orders.stream()
                .forEach();
    });

        return new ProductStat();
    };
    }

    @Bean
    public RepositoryItemWriter<ProductStat> vipWriter() {
      return new RepositoryItemWriterBuilder<ProductStat>()
              .repository(productStatJpaRepository)
              .methodName("save")
              .build();
    }


    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                // 주문 및 주문상품 데이터를 불러와서 해시맵 생성
                List<Order> orders = orderJpaRepository.findAllByDeliveredAtAndStatus(
                        LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX), OrderStatus.구매확정);

                Map<Long, List<OrderProduct>> orderProductMap = orders.stream()
                        .flatMap(order -> order.getOrderProducts().stream())
                        .collect(Collectors.groupingBy(orderProduct -> orderProduct.getProduct().getId()));

                jobExecution.getExecutionContext().put("orderProductMap", orderProductMap);
            }
        };
    }
}
