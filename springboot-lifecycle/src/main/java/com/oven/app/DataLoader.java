package com.oven.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

@Slf4j
public class DataLoader implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("=================Loading data...=================");
    }

}
