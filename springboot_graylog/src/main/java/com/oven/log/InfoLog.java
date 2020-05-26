package com.oven.log;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class InfoLog implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            log.info("--------------------------- 打印日志了。。。" + new DateTime().toString("HH:mm:ss"));
            TimeUnit.SECONDS.sleep(2);
        }
    }

}
