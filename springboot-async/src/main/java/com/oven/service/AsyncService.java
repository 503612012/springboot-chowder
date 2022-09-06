package com.oven.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AsyncService {

    @Async
    public void doSomething() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            System.out.println("do something..." + i);
            TimeUnit.SECONDS.sleep(1);
        }
    }

}
