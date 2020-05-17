# springboot炖ActiveMQ
### 1. 先睹为快
### 2. docker安装activemq
#### 2.1 搜索activemq
```shell script
docker search activemq
```
#### 2.2 下载activemq
```shell script
docker pull webcenter/activemq
```
#### 2.3 启动activemq
```shell script
docker run -d --name myactivemq -p 61616:61616 -p 8161:8161 webcenter/activemq
```
#### 2.4 设置客户端访问密码
##### 2.4.1 进入docker容器
```shell script
docker exec -it myactivemq /bin/bash
```
##### 2.4.2 编辑activemq.xml文件，在shutdownHooks下方添加：
```xml
<plugins>
    <simpleAuthenticationPlugin>
        <users>
            <authenticationUser username="${activemq.username}" password="${activemq.password}" groups="users,admins"/>
        </users>
    </simpleAuthenticationPlugin>
</plugins>
```
##### 2.4.3 修改credentials.properties文件：
```properties
activemq.username=admin
activemq.password=123456
guest.password=123456
```
##### 2.4.4 重启容器
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
        <artifactId>spring-boot-starter-activemq</artifactId>
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
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Destination;

@Component
public class ActiveMQProducer {

    @Resource
    private JmsMessagingTemplate messagingTemplate;

    public void send(Destination destination, String message) {
        messagingTemplate.convertAndSend(destination, message);
    }

}
```
#### 3.8 开发发送消息接口
```java
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
        Destination destination = new ActiveMQQueue("springboot_activemq");
        producer.send(destination, msg);
        return "发送成功！";
    }

}
```
#### 3.9 开发消息消费者
```java
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQConsumer {

    @JmsListener(destination = "springboot_activemq")
    public void consumer(String message) {
        System.out.printf("消费者收到消息：%s", message);
    }

}
```
#### 3.10 编写配置文件
```yaml
spring:
  activemq:
    broker-url: tcp://172.16.188.184:61616
    user: admin
    password: 123456
```
#### 3.11 编译打包运行
### 4. 应用场景