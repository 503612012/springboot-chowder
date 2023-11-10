package com.oven.mqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Slf4j
public class MQTTClient {

    private static MqttClient client;
    private final String host;
    private final String username;
    private final String password;
    private final String clientId;
    private final int timeout;
    private final int keepalive;

    public MQTTClient(String host, String username, String password, String clientId, int timeOut, int keepAlive) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.timeout = timeOut;
        this.keepalive = keepAlive;
    }

    public static MqttClient getClient() {
        return client;
    }

    public static void setClient(MqttClient client) {
        MQTTClient.client = client;
    }

    /**
     * 设置mqtt连接参数
     */
    public MqttConnectOptions setMqttConnectOptions(String username, String password, int timeout, int keepalive) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(timeout);
        options.setKeepAliveInterval(keepalive);
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        return options;
    }

    /**
     * 连接mqtt服务端
     */
    public void connect() throws MqttException {
        if (client == null) {
            client = new MqttClient(host, clientId, new MemoryPersistence());
            client.setCallback(new MQTTCallback(MQTTClient.this));
        }
        MqttConnectOptions mqttConnectOptions = setMqttConnectOptions(username, password, timeout, keepalive);
        if (!client.isConnected()) {
            client.connect(mqttConnectOptions);
        } else {
            client.disconnect();
            client.connect(mqttConnectOptions);
        }
        log.info("MQTT客户端连接成功");
    }

    /**
     * 发布消息，默认qos为0，非持久化
     */
    public void publish(String pushMessage, String topic) {
        publish(pushMessage, topic, 0, false);
    }

    /**
     * 发布消息
     */
    public void publish(String msg, String topic, int qos, boolean retained) {
        MqttMessage message = new MqttMessage();
        message.setPayload(msg.getBytes());
        message.setQos(qos);
        message.setRetained(retained);
        MqttTopic mqttTopic = MQTTClient.getClient().getTopic(topic);
        if (null == mqttTopic) {
            log.error("主题{}不存在", topic);
            return;
        }
        MqttDeliveryToken token;
        synchronized (this) { // 这里一定要同步，否则，在多线程publish的情况下，线程会发生死锁
            try {
                token = mqttTopic.publish(message); // 也是发送到执行队列中，等待执行线程执行，将消息发送到消息中间件
                token.waitForCompletion(1000L);
            } catch (MqttException e) {
                log.error("发布消息异常：", e);
            }
        }
    }

    /**
     * 订阅主题
     */
    public void subscribe(String topic, int qos) {
        try {
            MQTTClient.getClient().subscribe(topic, qos);
        } catch (MqttException e) {
            log.error("订阅主题异常：", e);
        }
    }

    /**
     * 取消订阅主题
     */
    public void unsubscribe(String topic) {
        if (client != null && client.isConnected()) {
            try {
                client.unsubscribe(topic);
            } catch (MqttException e) {
                log.error("取消订阅主题失败：", e);
            }
        }
    }

}
