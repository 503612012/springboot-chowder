package com.oven.controller;

import com.oven.producer.ActiveMQProducer;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jms.Destination;

@RestController
public class DemoController {

    @Resource
    private ActiveMQProducer producer;

    @RequestMapping("/send")
    public String send(String msg) {
        Destination destination = new ActiveMQQueue("springboot-activemq");
        producer.send(destination, msg);
        return "发送成功！";
    }

}
