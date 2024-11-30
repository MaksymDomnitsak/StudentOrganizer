package com.studyorganizer.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.studmodel")
public class NotesApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesApplication.class, args);
    }

}
