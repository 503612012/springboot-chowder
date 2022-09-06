# springboot炖sentinel

### 1. 先睹为快

### 2. 启动sentinel

#### 2.1 下载sentinel

```url
https://github.com/alibaba/Sentinel/releases
```

#### 2.2 启动sentinel

```shell script
java -jar sentinel-dashboard-1.8.2.jar
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
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        <version>2.1.0.RELEASE</version>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>Finchley.SR1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>0.2.2.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
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

#### 3.7 开发测试控制器
```java
package com.oven.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String test(String name) {
        return "hello " + name;
    }

}

```
#### 3.8 编辑配置文件

```properties
server.port=8888
spring.application.name=springboot-sentinel
spring.cloud.sentinel.transport.dashboard=localhost:8080
```

#### 3.9 编译打包运行

### 4. 应用场景