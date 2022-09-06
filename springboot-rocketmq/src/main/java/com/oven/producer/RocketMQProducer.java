package com.oven.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RocketMQProducer {

    @Value("${rocketmq.producer.groupName}")
    private String groupName;

    @Value("${rocketmq.namesrvAddr}")
    private String nameserAddr;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQProducer getRocketMQProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(groupName);
        producer.setNamesrvAddr(nameserAddr);
        producer.setVipChannelEnabled(false);
        return producer;
    }

}
