package com.oven.mqtt.controller;

import com.oven.mqtt.config.MQTTClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MqttController {

    @Resource
    private MQTTClient mqttClient;

    @ResponseBody
    @RequestMapping("/send")
    public void send(String msg) {
        mqttClient.publish(msg, "mqtt_topic");
        mqttClient.publish(msg, "/aicam/aisp/1923fdb39294684f/state");
    }

}
