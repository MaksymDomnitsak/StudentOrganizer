package com.studyorganizer.studgroups;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.studmodel")
public class StudGroupsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudGroupsApplication.class, args);
    }

}
