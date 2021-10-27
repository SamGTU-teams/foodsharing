package ru.rassafel.foodsharingconfigservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigServer
public class FoodsharingConfigServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(FoodsharingConfigServiceApplication.class, args);
	}
}
