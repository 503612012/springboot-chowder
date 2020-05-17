package com.oven.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQConsumer {

    @JmsListener(destination = "springboot_activemq")
    public void consumer(String message) {
        System.out.printf("消费者收到消息：%s", message);
    }

}
