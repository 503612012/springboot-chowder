package com.oven.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 启动类
 *
 * @author Oven
 */
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.oven")
public class Application {

    /**
     * 系统入口
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
