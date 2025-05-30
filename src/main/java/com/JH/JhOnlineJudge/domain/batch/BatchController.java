package com.JH.JhOnlineJudge.domain.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;


    @GetMapping("/vip")
    public ResponseEntity<String> vipBatch() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())  // 매번 다른 값으로
                .toJobParameters();
        jobLauncher.run(jobRegistry.getJob("updateVipJob"), jobParameters);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/stat")
    public ResponseEntity<String> statBatch(@RequestParam LocalDate date) throws Exception {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addLocalDate("date",date).toJobParameters();
        jobLauncher.run(jobRegistry.getJob("updateStatJob"), jobParametersBuilder.toJobParameters());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/refund")
    public ResponseEntity<String> refundBatch() throws Exception {
        jobLauncher.run(jobRegistry.getJob("refundRequestStep"), new JobParameters());

        return ResponseEntity.ok().build();
    }
}
