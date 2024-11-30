package com.studyorganizer.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.studmodel")
public class EventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventsApplication.class, args);
    }

}
