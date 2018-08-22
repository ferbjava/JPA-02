package com.capgemini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StarterKitJpa02StarterApplication {

	public static void main(String[] args) {
		// Uncomment line below to use mysql database (default database name = jstk_jpa_02, user = user_jpa_02, pass = JSTKjpa02)
		// you can change this in application-mysql.properties
		System.setProperty("spring.profiles.active", "mysql");

		SpringApplication.run(StarterKitJpa02StarterApplication.class, args);
	}
}
