package com.oven.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DebugLog implements CommandLineRunner {

    @Override
    public void run(String... args) {
        new Thread(() -> {
            while (true) {
                log.debug("debug日志。。。");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
