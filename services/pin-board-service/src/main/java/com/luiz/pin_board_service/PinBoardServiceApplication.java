package com.luiz.pin_board_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PinBoardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PinBoardServiceApplication.class, args);
	}

}
