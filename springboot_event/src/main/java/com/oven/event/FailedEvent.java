package com.oven.event;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

public class FailedEvent implements ApplicationListener<ApplicationFailedEvent> {

    @Override
    public void onApplicationEvent(ApplicationFailedEvent applicationFailedEvent) {
        System.out.println("=================6. FailedEvent=================");
    }

}
