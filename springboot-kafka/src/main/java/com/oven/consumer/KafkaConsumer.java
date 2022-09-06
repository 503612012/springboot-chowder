package com.oven.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = {"springboot-kafka"})
    public void consumer(String message) {
        System.out.printf("消费者接受到消息：%s", message);
    }

}
