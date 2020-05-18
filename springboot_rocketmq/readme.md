# springboot炖RabbitMQ
### 1. 先睹为快
### 2. docker安装rocketmq
#### 2.1 下载rocketmq镜像
```shell script
docker pull rocketmqinc/rocketmq
```
#### 2.2 启动name-server容器
```shell script
docker run -d -p 9876:9876 --name mqnamesrv -e "MAX_POSSIBLE_HEAP=100000000" rocketmqinc/rocketmq sh mqnamesrv
```
#### 2.3 编写配置文件
```shell script
echo "brokerIP1=172.16.188.194" > broker.properties
```
#### 2.4 启动broker容器
```shell script
docker run -d -p 10911:10911 -p 10909:10909 -v /root/broker.properties:/opt/rocketmq-4.4.0/bin/broker.properties --name mqbroker --link mqnamesrv -e "NAMESRV_ADDR=mqnamesrv:9876" -e "MAX_POSSIBLE_HEAP=200000000" rocketmqinc/rocketmq sh mqbroker -c broker.properties
```
#### 2.5 启动控制台容器
```shell script
docker run --link mqnamesrv -e "JAVA_OPTS=-Drocketmq.namesrv.addr=mqnamesrv:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -p 8080:8080 -t styletang/rocketmq-console-ng
```
### 3. 实现原理
#### 3.1 新建项目
#### 3.2 创建maven目录结构，以及pom.xml文件
#### 3.3 pom.xml文件中加入spring-boot-starter-parent
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.5.RELEASE</version>
    <relativePath/>
</parent>
```
#### 3.4 pom.xml文件中加入springboot-starter依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.apache.rocketmq</groupId>
        <artifactId>rocketmq-client</artifactId>
        <version>4.3.0</version>
    </dependency>
</dependencies>
```
#### 3.5 pom.xml文件中加入maven-springboot打包插件
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```
#### 3.6 开发启动类
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
#### 3.7 开发消息发送者
```java
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
```
#### 3.8 开发发送消息接口
```java
import com.oven.producer.RocketMQProducer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private RocketMQProducer rocketMQProducer;

    @RequestMapping("/send")
    public String send(String msg) {
        DefaultMQProducer producer = rocketMQProducer.getRocketMQProducer();
        Message message = new Message("springboot_rocketmq", "test", msg.getBytes());
        try {
            producer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "发送成功";
    }

}
```
#### 3.9 开发消息消费者
```java
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class RocketMQConsumer {

    @Resource
    private MessageListener messageListener;

    @Value("${rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.consumer.groupName}")
    private String groupName;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public DefaultMQPushConsumer getRocketMQConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setVipChannelEnabled(false);
        consumer.registerMessageListener(messageListener);
        try {
            consumer.subscribe("springboot_rocketmq", "test");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return consumer;
    }

}
```
#### 3.10 开发消息监听器
```java
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageListener implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        MessageExt ext = list.get(0);
        String msg = new String(ext.getBody());
        System.out.printf("消费者接受到消息：%s", msg);
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
```
#### 3.11 在RocketMQ控制台创建topic
#### 3.12 编写配置文件
```properties
rocketmq.namesrvAddr=172.16.188.194:9876
rocketmq.producer.groupName=producer
rocketmq.consumer.groupName=consumer
```
#### 3.13 编译打包运行
### 4. 应用场景