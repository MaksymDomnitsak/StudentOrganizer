package com.studyorganizer.userstokens;

import org.springframework.boot.SpringApplication;

public class TestUsersTokensApplication {

	public static void main(String[] args) {
		SpringApplication.from(UsersTokensApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
