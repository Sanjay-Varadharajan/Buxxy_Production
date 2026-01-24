package com.buxxy.buxxy_fraud_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BuxxyFraudEngineApplication {
	public static void main(String[] args) {
		SpringApplication.run(BuxxyFraudEngineApplication.class, args);
	}
}
