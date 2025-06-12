package com.luiz.pin_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PinServiceApplication.class, args);
	}

}
