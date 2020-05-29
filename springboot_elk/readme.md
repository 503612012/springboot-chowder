# springboot炖elk
### 1. 先睹为快
### 2. centos安装elk
#### 2.1 安装elasticsearch
##### 2.1.1 下载elasticsearch-6.1.1.tar.gz
##### 2.1.2 解压并进入es根目录
##### 2.1.3 创建用户组、用户，并赋予权限
```shell script
groupadd elsearch
useradd elsearch -g elsearch -p elasticsearch
chown -R elsearch:elsearch elasticsearch
```
##### 2.1.4 使用root用户编辑配置文件
```shell script
vim /etc/security/limits.conf
```
##### 2.1.5 末尾追加(xxx为上边添加的用户)
```
xxx hard nofile 65536
xxx soft nofile 65536
```
##### 2.1.6 切换到root用户
```bash
sysctl -w vm.max_map_count=262144
```
##### 2.1.7 查看结果
```bash
sysctl -a|grep vm.max_map_count
```
##### 2.1.8 显示如下表示成功
```bash
vm.max_map_count = 262144
```
##### 2.1.9 上述方法修改之后，如果重启虚拟机将失效，解决办法是在/etc/sysctl.conf文件最后添加一行
```bash
vm.max_map_count=262144
```
##### 2.1.10 然后让其生效
```bash
sysctl -p
```
##### 2.1.11 修改es配置文件
```properties
network.host: 0.0.0.0
```
##### 2.1.12 末尾追加
```properties
cluster.routing.allocation.disk.threshold_enabled: false
```
##### 2.1.13 使用新添加的用户启动es
```shell script
bin/elasticsearch
```
#### 2.2 安装kibnana
##### 2.2.1 下载kibana-6.1.1-linux-x86_64.tar.gz
##### 2.2.2 修改配置文件
```properties
server.port: 5601
server.host: 172.16.188.142
elasticsearch.url: "http://172.16.188.142:9200"
```
##### 2.2.3 启动
```shell script
bin/kibana
```
#### 2.3 安装logstash
##### 2.3.1 下载logstash-6.1.1.tar.gz
##### 2.3.2 在logstash根目录新增日志配置文件
```
input {
  tcp {
    mode => "server"
    host => "0.0.0.0"
    port => 4560
    codec => json_lines
  }
}
output {
  elasticsearch {
    hosts => "localhost:9200"
    index => "springboot-logstash-%{+YYYY.MM.dd}"
  }
}
```
##### 2.3.3 使用新增的配置文件启动logstash
```shell script
bin/logstash -f logConfig
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
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <dependency>
        <groupId>net.logstash.logback</groupId>
        <artifactId>logstash-logback-encoder</artifactId>
        <version>6.1</version>
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
#### 3.7 开发测试接口类
```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DemoController {

    @RequestMapping("/test")
    public String test(String key) {
        log.info("打印日志了。。。{}", key);
        return "打印日志成功";
    }

}
```
#### 3.8 编写配置文件logback-spring.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/base.xml"/>

    <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
        <destination>172.16.188.194:4560</destination>
        <!-- 日志输出编码 -->
        <encoder charset="UTF-8" class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp>
                    <timeZone>UTC</timeZone>
                </timestamp>
                <pattern>
                    <pattern>
                        {
                        "logLevel": "%level",
                        "serviceName": "${springAppName:-}",
                        "pid": "${PID:-}",
                        "thread": "%thread",
                        "class": "%logger{40}",
                        "rest": "%message"
                        }
                    </pattern>
                </pattern>
            </providers>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="LOGSTASH"/>
        <appender-ref ref="CONSOLE"/>
    </root>

</configuration>
```
#### 3.9 启动项目
#### 3.10 登录kibnana页面(http://ip:5601)添加index pattern
#### 3.11 调用日志接口，在kibnana控制台观察效果
### 4. 应用场景