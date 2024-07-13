package com.elyashevich.network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetworkApplication.class, args);
	}

}
