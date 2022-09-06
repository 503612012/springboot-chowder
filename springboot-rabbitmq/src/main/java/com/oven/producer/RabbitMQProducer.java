package com.oven.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RabbitMQProducer {

    @Resource
    private AmqpTemplate amqpTemplate;

    public void send(String queue, String message) {
        amqpTemplate.convertAndSend(queue, message);
    }

}
