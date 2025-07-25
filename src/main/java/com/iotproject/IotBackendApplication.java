package com.iotproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.iotproject.model")
@EnableJpaRepositories("com.iotproject.repository")
public class IotBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotBackendApplication.class, args);
	}

}
