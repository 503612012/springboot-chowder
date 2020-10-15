package com.oven.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.oven")
public class Application {

    public static void main(String[] args) {
        System.out.println("=================before start=================");
        SpringApplication.run(Application.class, args);
        System.out.println("=================after start=================");
    }

    @Bean
    public DataLoader dataLoader() {
        System.out.println("=================before autowire=================");
        return new DataLoader();
    }

}
