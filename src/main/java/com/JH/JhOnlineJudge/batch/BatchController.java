package com.JH.JhOnlineJudge.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

        jobLauncher.run(jobRegistry.getJob("updateVipJob"), new JobParameters());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/stat")
    public ResponseEntity<String> statBatch() throws Exception {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addLocalDate("date",LocalDate.now()).toJobParameters();

        jobLauncher.run(jobRegistry.getJob("updateStatJob"), jobParametersBuilder.toJobParameters());

        jobParametersBuilder.addLocalDate("date",LocalDate.now().plusDays(1)).toJobParameters();

        jobLauncher.run(jobRegistry.getJob("updateStatJob"), jobParametersBuilder.toJobParameters());

        return ResponseEntity.ok().build();
    }
}
