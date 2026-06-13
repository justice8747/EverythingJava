package com.justice.microservice;

import org.springframework.boot.SpringApplication;

public class TestMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(MicroserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
