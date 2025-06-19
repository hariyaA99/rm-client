package com.mphasis.rmclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients(basePackages = "com.mphasis.rmclient.feign")
@SpringBootApplication
@EntityScan("com.mphasis.rmclient.entity")
@EnableJpaRepositories("com.mphasis.rmclient.repository")
@ComponentScan("com.mphasis.rmclient")
public class RMClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(RMClientApplication.class, args);
	}
}
