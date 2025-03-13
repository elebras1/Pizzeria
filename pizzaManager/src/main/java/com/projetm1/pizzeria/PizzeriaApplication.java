package com.projetm1.pizzeria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.projetm1.pizzeria")
@EnableJpaRepositories(basePackages = "com.projetm1.pizzeria")
@EnableMongoRepositories(basePackages = "com.projetm1.pizzeria.commentaire")
public class PizzeriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzeriaApplication.class, args);
	}

}
