# springboot炖memcached
### 1. 先睹为快
### 2. 实现原理
#### 2.1 docker中安装memcache环境
##### 2.1.1 拉取镜像
```shell script
docker pull memcached
```
##### 2.1.2 启动容器
```shell script
docker run -p 11211:11211 --name my-memcache -d memcached
```
#### 2.2 新建项目
#### 2.2.1 创建maven目录结构，以及pom.xml文件
#### 2.2.2 pom.xml文件中加入依赖
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.5.RELEASE</version>
    <relativePath/>
</parent>
```
#### 2.2.3 pom.xml文件中加入springboot-starter依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>net.spy</groupId>
        <artifactId>spymemcached</artifactId>
        <version>2.12.2</version>
    </dependency>
</dependencies>
```
#### 2.2.4 pom.xml文件中加入maven-springboot打包插件
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
#### 2.2.5 开发启动类
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
#### 2.2.6 开发配置文件读取类
```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "springboot.memcache")
public class MemcacheSource {

    private String ip;
    private int port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
```
#### 2.2.7 开发memcache配置类
```java
import net.spy.memcached.MemcachedClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;

@Component
public class MemcachedRunner implements CommandLineRunner {

    @Resource
    private MemcacheSource memcacheSource;

    private MemcachedClient client = null;

    @Override
    public void run(String... args) {
        try {
            client = new MemcachedClient(new InetSocketAddress(memcacheSource.getIp(), memcacheSource.getPort()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MemcachedClient getClient() {
        return client;
    }

}
```
#### 2.2.8 开发测试控制层
```java
import com.oven.config.MemcachedRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private MemcachedRunner memcachedRunner;

    @RequestMapping("/set")
    public void set(String key, String value) {
        memcachedRunner.getClient().set(key, 1000, value);
    }

    @RequestMapping("/get")
    public String get(String key) {
        return memcachedRunner.getClient().get(key).toString();
    }

}
```
#### 2.2.9 编写配置文件
```properties
springboot.memcache.ip=192.168.63.2
springboot.memcache.port=11211
```
#### 2.3 编译打包运行
### 3. 应用场景