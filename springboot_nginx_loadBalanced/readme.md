# springboot炖nginx负载均衡
### 1. 先睹为快
### 2. 安装nginx
#### 2.1 下载nginx
```http request
http://nginx.org
```
#### 2.2 创建安装目录
```shell script
mkdir /usr/local/nginx
```
#### 2.3 安装nginx依赖包
```shell script
yum install -y gcc-c++ pcre-devel zlib-devel
```
#### 2.4 检查并指定安装
```shell script
./configure --prefix=/usr/local/nginx
```
#### 2.5 预编译
```shell script
make
```
#### 2.6 编译安装
```shell script
make install
```
#### 2.7 编译安装
```
upstream springboot_nginx_loadBalance {
    server localhost:8080;
    server localhost:8081;
    server localhost:8082;
}

location / {
    proxy_pass http://springboot_nginx_loadBalance;
    proxy_redirect default;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
}
```
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
#### 2.7 开发测试接口
```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/test")
    public String test() {
        return "hello " + port;
    }

}
```
#### 2.8 开发配置文件
```yaml
server:
  port: 8080
```
#### 2.9 编译打包运行
### 3. 应用场景