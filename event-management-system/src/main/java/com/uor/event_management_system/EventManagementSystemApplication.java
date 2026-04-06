package com.uor.event_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EventManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventManagementSystemApplication.class, args);
	}

}
