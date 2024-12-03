package com.JH.JhOnlineJudge.batch;

import com.JH.JhOnlineJudge.common.statistic.StatDateRange;
import com.JH.JhOnlineJudge.common.statistic.StatDateRangeHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class BatchSchedule {
        private final JobLauncher jobLauncher;
        private final JobRegistry jobRegistry;

        @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
        public void runVipJob() throws Exception {
            jobLauncher.run(jobRegistry.getJob("updateVipJob"), new JobParameters());
        }

        @Scheduled(cron = "0 0 22 * * ?", zone = "Asia/Seoul")
        public void runStatBatch() throws Exception {

            jobLauncher.run(jobRegistry.getJob("updateStatJob"), new JobParameters());
        }

}
