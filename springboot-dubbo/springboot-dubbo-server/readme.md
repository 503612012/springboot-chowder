# springboot炖dubbo-server
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
        <groupId>com.oven</groupId>
        <artifactId>springboot-dubbo-common-api</artifactId>
        <version>${project.version}</version>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.apache.dubbo</groupId>
        <artifactId>dubbo-spring-boot-starter</artifactId>
        <version>${dubbo.version}</version>
    </dependency>

    <dependency>
        <groupId>org.apache.zookeeper</groupId>
        <artifactId>zookeeper</artifactId>
        <version>${zookeeper.version}</version>
    </dependency>

    <dependency>
        <groupId>org.apache.curator</groupId>
        <artifactId>curator-recipes</artifactId>
        <version>${curator.version}</version>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
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
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@DubboComponentScan("com.oven")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
#### 3.7 开发服务接口实现类
```java
import com.oven.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Service(version = "1.0.0")
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        log.info("dubbo服务端被调用了：{}", name);
        return "hello " + name;
    }

}

```
#### 3.8 编辑配置文件
```properties
server.port=8080
dubbo.application.name=demo-provider
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880
dubbo.registry.address=zookeeper://xxx.xxx.xxx.xxx:2181
```
#### 3.9 编译打包运行
### 4. 应用场景