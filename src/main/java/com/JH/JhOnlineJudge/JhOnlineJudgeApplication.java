package com.JH.JhOnlineJudge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication (exclude = SecurityAutoConfiguration.class)
public class JhOnlineJudgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JhOnlineJudgeApplication.class, args);
	}

}
