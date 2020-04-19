package com.oven;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner2 implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("项目启动后就会执行。。。222");
    }

}
