# springboot炖logback
### 1. 先睹为快
### 2. 实现原理
#### 2.1 新建项目
#### 2.2 创建maven目录结构，以及pom.xml文件
#### 2.3 pom.xml文件中加入依赖
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.5.RELEASE</version>
    <relativePath/>
</parent>
```
#### 2.4 pom.xml文件中加入springboot-starter依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```
#### 2.5 pom.xml文件中加入maven-springboot打包插件
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
#### 2.6 开发启动类
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
#### 2.7 开发debug日志类
```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DebugLog implements CommandLineRunner {

    @Override
    public void run(String... args) {
        new Thread(() -> {
            while (true) {
                log.debug("debug日志。。。");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
```
#### 2.8 开发info日志类
```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class InfoLog implements CommandLineRunner {

    @Override
    public void run(String... args) {
        new Thread(() -> {
            while (true) {
                log.info("info日志。。。");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
```
#### 2.9 开发error日志类
```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ErrorLog implements CommandLineRunner {

    @Override
    public void run(String... args) {
        new Thread(() -> {
            while (true) {
                log.error("error日志。。。");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
```
#### 2.10 开发stdout日志类
```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class StdoutLog implements CommandLineRunner {

    @Override
    public void run(String... args) {
        new Thread(() -> {
            while (true) {
                log.info("stdout日志。。。");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
```
#### 2.11 开发logback配置文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <property name="LOG_HOME" value="/Users/oven/log/springboot-logback"/>
    <property name="PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger.%M:%L - %msg%n"/>

    <!-- DEBUG日志定义 -->
    <appender name="DEBUG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_HOME}/springboot-logback.debug.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_HOME}/springboot-logback.debug.%d{yyyy-MM-dd}-%i.log</fileNamePattern>
            <maxHistory>180</maxHistory>
            <maxFileSize>10MB</maxFileSize>
            <totalSizeCap>20GB</totalSizeCap>
            <cleanHistoryOnStart>true</cleanHistoryOnStart>
        </rollingPolicy>
        <encoder>
            <pattern>${PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!-- 异步DEBUG日志定义 -->
    <appender name="SYNC_DEBUG" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="DEBUG"/>
        <includeCallerData>true</includeCallerData>
    </appender>

    <!-- INFO日志定义 -->
    <appender name="INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_HOME}/springboot-logback.info.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_HOME}/springboot-logback.info.%d{yyyy-MM-dd}-%i.log</fileNamePattern>
            <maxHistory>180</maxHistory>
            <maxFileSize>10MB</maxFileSize>
            <totalSizeCap>20GB</totalSizeCap>
            <cleanHistoryOnStart>true</cleanHistoryOnStart>
        </rollingPolicy>
        <encoder>
            <pattern>${PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!-- 异步INFO日志定义 -->
    <appender name="SYNC_INFO" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="INFO"/>
        <includeCallerData>true</includeCallerData>
    </appender>

    <!-- ERROR日志定义 -->
    <appender name="ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_HOME}/springboot-logback.error.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_HOME}/springboot-logback.error.%d{yyyy-MM-dd}-%i.log</fileNamePattern>
            <maxHistory>180</maxHistory>
            <maxFileSize>10MB</maxFileSize>
            <totalSizeCap>20GB</totalSizeCap>
            <cleanHistoryOnStart>true</cleanHistoryOnStart>
        </rollingPolicy>
        <encoder>
            <pattern>${PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!-- 异步ERROR日志定义 -->
    <appender name="SYNC_ERROR" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="ERROR"/>
        <includeCallerData>true</includeCallerData>
    </appender>

    <!-- 定义控制台日志信息 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%red(%d{yyyy-MM-dd HH:mm:ss}) %green([%thread]) %highlight(%-5level) %boldMagenta(%logger{10}).%M:%L - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- 定义异步控制台日志信息 -->
    <appender name="SYNC_STDOUT" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="STDOUT"/>
        <includeCallerData>true</includeCallerData>
    </appender>

    <root level="INFO">
        <appender-ref ref="SYNC_STDOUT"/>
    </root>
    <logger name="com.oven.log.DebugLog" level="DEBUG" additivity="false">
        <appender-ref ref="SYNC_DEBUG"/>
    </logger>
    <logger name="com.oven.log.InfoLog" level="INFO" additivity="false">
        <appender-ref ref="SYNC_INFO"/>
    </logger>
    <logger name="com.oven.log.ErrorLog" level="ERROR" additivity="false">
        <appender-ref ref="SYNC_ERROR"/>
    </logger>

</configuration>
```
#### 2.12 编译打包运行
### 3. 应用场景