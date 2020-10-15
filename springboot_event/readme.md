# springboot炖event
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
        <artifactId>spring-boot-starter-web</artifactId>
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
#### 2.6 开发commandLineRunner
 ```java
import org.springframework.boot.CommandLineRunner;

public class DataLoader implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("=================Loading data...=================");
    }

}
```
#### 2.7 开发启动类
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.oven")
public class Application {

    public static void main(String[] args) {
        System.out.println("=================before start=================");
        SpringApplication.run(Application.class, args);
        System.out.println("=================after start=================");
    }

    @Bean
    public DataLoader dataLoader() {
        System.out.println("=================before autowire=================");
        return new DataLoader();
    }

}
```
#### 2.8 开发ApplicationEnvironmentPreparedEvent事件类
```java
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

public class EnvironmentPreparedEvent implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        System.out.println("=================2. EnvironmentPreparedEvent=================");
    }

}
```
#### 2.9 开发ApplicationFailedEvent事件类
```java
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

public class FailedEvent implements ApplicationListener<ApplicationFailedEvent> {

    @Override
    public void onApplicationEvent(ApplicationFailedEvent applicationFailedEvent) {
        System.out.println("=================6. FailedEvent=================");
    }

}
```
#### 2.10 开发ApplicationPreparedEvent事件类
```java
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

public class PreparedEvent implements ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
        System.out.println("=================3. PreparedEvent=================");
    }

}
```
#### 2.11 开发ApplicationReadyEvent事件类
```java
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

public class ReadyEvent implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("=================5. ReadyEvent=================");
    }

}
```
#### 2.12 开发ApplicationStartedEvent事件类
```java
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

public class StartedEvent implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        System.out.println("=================4. StartedEvent=================");
    }

}
```
#### 2.13 开发ApplicationStartingEvent事件类
```java
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

public class StartingEvent implements ApplicationListener<ApplicationStartingEvent> {


    @Override
    public void onApplicationEvent(ApplicationStartingEvent applicationStartingEvent) {
        System.out.println("=================1. StartingEvent=================");
    }

}
```
#### 2.14 开发测试控制层
```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    @ResponseBody
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

}
```
#### 2.15 添加事件配置文件
##### 2.15.1 在resources目录下新建META-INF目录
##### 2.15.2 在META-INF目录新建spring.factories
```properties
org.springframework.context.ApplicationListener=com.oven.event.EnvironmentPreparedEvent,\
  com.oven.event.FailedEvent,\
  com.oven.event.PreparedEvent,\
  com.oven.event.ReadyEvent,\
  com.oven.event.StartedEvent,\
  com.oven.event.StartingEvent
```
#### 2.16 编译打包运行
### 3. 应用场景