package com.studyorganizer.userstokens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.studmodel")
public class UsersTokensApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersTokensApplication.class, args);
	}

}
