package com.oven.mqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MQTTCallback implements MqttCallbackExtended {

    private final MQTTClient mqttClient;

    public MQTTCallback(MQTTClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    /**
     * 丢失连接，可在这里做重连，只会调用一次
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.error("MQTT连接断开，5S之后尝试重连: {}", throwable.getMessage());
        long reconnectTimes = 1;
        while (true) {
            try {
                if (MQTTClient.getClient().isConnected()) {
                    log.info("MQTT重连成功");
                    return;
                }
                reconnectTimes += 1;
                log.warn("MQTT重新连接次数：{}，重新连接时间：{}", reconnectTimes, reconnectTimes);
                MQTTClient.getClient().reconnect();
            } catch (MqttException e) {
                log.error("MQTT重连异常", e);
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * 消息处理
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        log.info("接收消息主题 : {}，接收消息内容 : {}", topic, new String(mqttMessage.getPayload()));
    }

    /**
     * 连接成功后的回调
     */
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        log.info("MQTT连接成功，连接地址：{}，连接方式：{}", serverURI, reconnect ? "重连" : "直连");
        // 订阅主题
        mqttClient.subscribe("mqtt_topic", 1);
        mqttClient.subscribe("/aicam/aisp/+/state", 0);
    }

    /**
     * 消息到达后回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

}
