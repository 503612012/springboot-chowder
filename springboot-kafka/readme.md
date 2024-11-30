# springboot炖kafka
### 1. 先睹为快
### 2. docker安装kafka
#### 2.1 下载zookeeper镜像
```shell script
docker pull wurstmeister/zookeeper
```
#### 2.2 启动zookeeper容器
```shell script
docker run -d --name myzookeeper -p 2181:2181 -v /etc/localtime:/etc/localtime wurstmeister/zookeeper
```
#### 2.3 下载kafka镜像
```shell script
docker pull wurstmeister/kafka
```
#### 2.4 启动kafka容器
```shell script
docker run -d --name mykafka -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=192.168.63.2:2181/kafka -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://192.168.63.2:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -v /etc/localtime:/etc/localtime wurstmeister/kafka
```
#### 2.5 监控topic消息
```shell script
bin/kafka-console-consumer.sh --bootstrap-server 192.168.63.2:9092 --topic springboot-kafka
```
### 3. 实现原理
#### 3.1 新建项目
#### 3.2 创建maven目录结构，以及pom.xml文件
#### 3.3 pom.xml文件中加入依赖
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
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

}
```
#### 3.8 开发发送消息接口
```java
import com.oven.producer.KafkaProducer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private KafkaProducer producer;

    @RequestMapping("/send")
    public String send(String msg) {
        producer.send("springboot-kafka", msg);
        return "发送成功";
    }

}
```
#### 3.9 开发消息消费者
```java
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = {"springboot-kafka"})
    public void consumer(String message) {
        System.out.printf("消费者接受到消息：%s", message);
    }

}
```
#### 3.10 编写配置文件
```properties
spring.kafka.producer.bootstrap-servers=192.168.63.2:9092
spring.kafka.consumer.bootstrap-servers=${spring.kafka.producer.bootstrap-servers}
spring.kafka.consumer.group-id=consumer_group
spring.kafka.consumer.enable-auto-commit=true
spring.kafka.consumer.auto-offset-reset=latest
```
#### 3.11 编译打包运行
### 4. 应用场景