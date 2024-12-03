package com.JH.JhOnlineJudge.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;


    @GetMapping("/vip")
    public ResponseEntity<String> vipBatch() throws Exception {

        jobLauncher.run(jobRegistry.getJob("updateVipJob"), new JobParameters());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/stat")
    public ResponseEntity<String> statBatch() throws Exception {

        JobParameters jobParameters = new JobParameters();
        jobParameters.getLocalDateTime(LocalDateTime.now().toString());
        jobLauncher.run(jobRegistry.getJob("updateStatJob"),jobParameters);

        return ResponseEntity.ok().build();
    }
}
