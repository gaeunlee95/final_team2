package com.khit.recruit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Final2teamApplication {

	public static void main(String[] args) {
		SpringApplication.run(Final2teamApplication.class, args);
	}

}
