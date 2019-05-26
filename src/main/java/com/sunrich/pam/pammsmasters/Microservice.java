package com.sunrich.pam.pammsmasters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Microservice {

	public static void main(String[] args) {
		SpringApplication.run(Microservice.class, args);
	}
}
