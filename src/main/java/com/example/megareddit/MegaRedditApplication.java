package com.example.megareddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MegaRedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(MegaRedditApplication.class, args);
	}

}
