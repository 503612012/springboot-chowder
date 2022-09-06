package com.oven.producer;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Destination;

@Component
public class ActiveMQProducer {

    @Resource
    private JmsMessagingTemplate messagingTemplate;

    public void send(Destination destination, String message) {
        messagingTemplate.convertAndSend(destination, message);
    }

}
