# springboot炖dubbo-client
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
        <groupId>com.oven</groupId>
        <artifactId>springboot_dubbo_common_api</artifactId>
        <version>${project.version}</version>
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
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
#### 2.7 开发测试接口类
```java
import com.oven.service.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Reference(version = "1.0.0")
    private DemoService demoService;

    @RequestMapping("/test")
    public String test(String name) {
        return demoService.sayHello(name);
    }

}
```
#### 2.8 编辑配置文件
```properties
server.port=8081
dubbo.application.id=client
dubbo.registry.address=zookeeper://172.16.188.194:2181
```
#### 2.9 编译打包运行
### 3. 应用场景