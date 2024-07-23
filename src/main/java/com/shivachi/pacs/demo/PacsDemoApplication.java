package com.shivachi.pacs.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class PacsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PacsDemoApplication.class, args);
	}

}
