package com.JH.JhOnlineJudge.common.config;

import com.JH.JhOnlineJudge.domain.batch.VipChunkListener;
import com.JH.JhOnlineJudge.domain.batch.VipProcessor;
import com.JH.JhOnlineJudge.domain.batch.VipReader;
import com.JH.JhOnlineJudge.domain.user.entity.User;
import com.JH.JhOnlineJudge.domain.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class VipBatchConfig {


    private final JobRepository jobRepository;
    private final UserJpaRepository userJpaRepository;
    private final PlatformTransactionManager platformTransactionManager ;


    @Bean
     public Job updateVipJob(Step updateVipStep) {
         return new JobBuilder("updateVipJob",jobRepository)
                 .start(updateVipStep)
                 .build();
     }

    @Bean
      public Step updateVipStep(VipReader userReader,
                                VipProcessor vipProcessor,
                                ItemWriter<User> vipWriter,
                                VipChunkListener vipChunkListener) {
          return new StepBuilder("updateVipStep",jobRepository)
                  .<User, User>chunk(10000, platformTransactionManager)
                  .reader(userReader)
                  .processor(vipProcessor)
                  .writer(vipWriter)
                  .listener(vipChunkListener)
                  .allowStartIfComplete(true)
                  .build();
      }

      @Bean
      public RepositoryItemWriter<User> vipWriter() {
          return new RepositoryItemWriterBuilder<User>()
                  .repository(userJpaRepository)
                  .methodName("save")
                  .build();
      }

}
