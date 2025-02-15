package com.JH.JhOnlineJudge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication (exclude = SecurityAutoConfiguration.class)
@EnableRetry
public class JhOnlineJudgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JhOnlineJudgeApplication.class, args);
	}

}
