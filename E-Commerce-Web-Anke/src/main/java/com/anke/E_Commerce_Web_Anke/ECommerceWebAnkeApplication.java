package com.anke.E_Commerce_Web_Anke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ECommerceWebAnkeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceWebAnkeApplication.class, args);
	}

}
