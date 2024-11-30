package com.studyorganizer.googleschedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.studmodel")
public class GoogleScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoogleScheduleApplication.class, args);
    }

}
