package com.oven.netty.study;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudyServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StudyServerApplication.class, args);
    }

    @Override
    public void run(String... args) {
    }

}