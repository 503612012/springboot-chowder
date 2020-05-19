package com.oven.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

}
