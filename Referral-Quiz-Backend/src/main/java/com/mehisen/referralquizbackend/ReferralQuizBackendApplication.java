package com.mehisen.referralquizbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReferralQuizBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReferralQuizBackendApplication.class, args);
	}
}
