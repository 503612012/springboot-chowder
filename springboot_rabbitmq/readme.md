# springboot炖RabbitMQ
### 1. 先睹为快
### 2. docker安装rabbitmq
#### 2.1 下载rabbitmq镜像
```shell script
docker pull rabbitmq:management
```
#### 2.2 启动rabbitmq容器
```shell script
docker run -d --name myrabbitmq -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=123456 -p 15672:15672 -p 5672:5672 rabbitmq:management
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
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
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
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RabbitMQProducer {

    @Resource
    private AmqpTemplate amqpTemplate;

    public void send(String queue, String message) {
        amqpTemplate.convertAndSend(queue, message);
    }

}
```
#### 3.8 开发发送消息接口
```java
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
```
#### 3.9 开发消息消费者
```java
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "springboot_rabbitmq")
public class RabbitMQConsumer {

    @RabbitHandler
    public void consumer(String message) {
        System.out.printf("消费者接受到消息：%s", message);
    }

}
```
#### 3.10 在RabbitMQ控制台创建queue
#### 3.11 编写配置文件
```yaml
spring:
  rabbitmq:
    host: 172.16.188.194
    port: 5672
    username: admin
    password: 123456
```
#### 3.12 编译打包运行
### 4. 应用场景