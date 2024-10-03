package com.teleo.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class SambaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SambaApplication.class, args);
	}
}
