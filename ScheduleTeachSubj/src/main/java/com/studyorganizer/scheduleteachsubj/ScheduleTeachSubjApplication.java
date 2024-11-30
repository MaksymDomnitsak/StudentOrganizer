package com.studyorganizer.scheduleteachsubj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.studmodel")
public class ScheduleTeachSubjApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleTeachSubjApplication.class, args);
    }

}
