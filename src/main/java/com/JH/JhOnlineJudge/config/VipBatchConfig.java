package com.JH.JhOnlineJudge.config;

import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import com.JH.JhOnlineJudge.order.repository.OrderJpaRepository;
import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.domain.UserRole;
import com.JH.JhOnlineJudge.user.repository.UserJpaRepository;
import com.JH.JhOnlineJudge.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class VipBatchConfig {


    private final JobRepository jobRepository;
    private final UserJpaRepository userJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final PlatformTransactionManager platformTransactionManager ;


    @Bean
     public Job updateVipJob(Step updateVipStep) {
         return new JobBuilder("updateVipJob",jobRepository)
                 .start(updateVipStep)
                 .build();
     }

    @Bean
      public Step updateVipStep(ItemReader<User> userReader,
                                ItemProcessor<User, User> vipProcessor,
                                ItemWriter<User> vipWriter) {
          return new StepBuilder("updateVipStep",jobRepository)
                  .<User, User>chunk(10, platformTransactionManager)
                  .reader(userReader)
                  .processor(vipProcessor)
                  .writer(vipWriter)
                  .allowStartIfComplete(true)
                  .build();
      }

    @Bean
      public RepositoryItemReader<User> userReader() {
          return new RepositoryItemReaderBuilder<User>()
                  .name("userReader")
                  .pageSize(10)
                  .methodName("findAll")
                  .repository(userJpaRepository)
                  .sorts(Map.of("id", Sort.Direction.ASC))
                  .build();

      }

    @Bean
    public ItemProcessor<User, User> userVipProcessor() {
        return user -> {
            LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay();
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
            Integer totalSpentThisMonth = orderJpaRepository.sumOrdersByUserAndStatusAndDate(user.getId(), OrderStatus.구매확정, startOfMonth, endOfMonth);

            if(totalSpentThisMonth == null) { totalSpentThisMonth = 0; }
            System.out.println("user.getId() + totalSpentThisMonth = " + user.getId() + totalSpentThisMonth);
            UserRole role = totalSpentThisMonth >= 300000 ? UserRole.VIP고객님 : UserRole.고객님;
            if(user.getRole() != UserRole.관리자){
                user.updateRole(role);
            }

            return user;
        };
    }

      @Bean
      public RepositoryItemWriter<User> vipWriter() {
          return new RepositoryItemWriterBuilder<User>()
                  .repository(userJpaRepository)
                  .methodName("save")
                  .build();
      }

}
