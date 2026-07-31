package com.sujal.bud_repo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.sujal.bud_repo")
@SpringBootApplication
public class BudRepoApplication {

	public static void main(String[] args) throws Exception {

		SpringApplication.run(BudRepoApplication.class, args);
	}
}