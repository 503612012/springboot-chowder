package com.oven.controller;

import com.oven.producer.RabbitMQProducer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private RabbitMQProducer producer;

    @RequestMapping("/send")
    public String send(String msg) {
        producer.send("springboot_rabbitmq", msg);
        return "发送成功";
    }

}
