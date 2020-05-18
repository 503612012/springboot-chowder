package com.oven.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "springboot_rabbitmq")
public class RabbitMQConsumer {

    @RabbitHandler
    public void consumer(String message) {
        System.out.printf("消费者接受到消息：%s", message);
    }

}
