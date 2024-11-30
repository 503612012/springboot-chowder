package com.oven;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@DubboComponentScan("com.oven")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
