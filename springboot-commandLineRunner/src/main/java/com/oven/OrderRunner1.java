package com.oven;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class OrderRunner1 implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("项目启动后就会执行。。。order。。。111");
    }

}
