package com.oven.mqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MQTTConfiguration {

    @Bean
    public MQTTClient myMQTTClient(@Qualifier("MQTTProperties") MQTTProperties mqttProperties) {
        MQTTClient mqttClient = new MQTTClient(mqttProperties.getHost(), mqttProperties.getUsername(), mqttProperties.getPassword(), mqttProperties.getClientId(), mqttProperties.getTimeout(), mqttProperties.getKeepalive());
        try {
            mqttClient.connect();
            return mqttClient;
        } catch (MqttException e) {
            log.error("MQTT链接异常：", e);
        }
        return mqttClient;
    }

}
