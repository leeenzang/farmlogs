package com.ieunjin.farmlogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FarmlogsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmlogsApplication.class, args);
	}

}
