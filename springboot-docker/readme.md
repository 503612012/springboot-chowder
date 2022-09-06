# springboot炖docker
### 1. 先睹为快
### 2. 实现原理
#### 2.1 新建文件夹demo，为项目根目录
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
#### 2.4 pom.xml文件中加入springboot-web依赖
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
#### 2.6 pom.xml文件中加入docker打包插件
```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>1.2.0</version>
            <executions>
                <execution>
                    <id>build-image</id>
                    <phase>package</phase>
                    <goals>
                        <goal>build</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <dockerHost>http://172.16.188.194:2375</dockerHost>
                <imageName>docker/${project.artifactId}</imageName>
                <imageTags>
                    <imageTag>${project.version}</imageTag>
                </imageTags>
                <forceTags>true</forceTags>
                <dockerDirectory>${project.basedir}</dockerDirectory>
                <resources>
                    <resource>
                        <targetPath>/</targetPath>
                        <directory>${project.build.directory}</directory>
                        <include>${project.build.finalName}.jar</include>
                    </resource>
                </resources>
            </configuration>
        </plugin>
    </plugins>
</build>
```
#### 2.7 根目录添加Dockerfile文件
```dockerfile
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/springboot-docker-1.0.0.jar docker.jar
ENTRYPOINT ["java","-jar","/docker.jar"]
```
#### 2.8 开发启动类
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
#### 2.9 开发测试控制器
```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping("/test")
    public Object test(String name) {
        return "hello " + name;
    }

}
```
#### 2.10 docker开启远程访问，编辑/usr/lib/systemd/system/docker.service文件
```
ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375  -H unix:///var/run/docker.sock
```
#### 2.11 重启docker
```shell script
systemctl daemon-reload
systemctl restart docker
```
#### 2.12 idea安装docker插件
#### 2.13 idea配置远程docker
```
Docker -> TCP socker -> tcp://172.16.188.194:2375
```
#### 2.14 编写配置文件
```properties
server.port=8990
```
#### 2.15 编译打包运行
```shell script
mvn package
```
### 3. 防火墙操作
#### 3.1 添加端口
```shell script
firewall-cmd --zone=public --add-port=8888/tcp --permanent
```
#### 3.1 重载
```shell script
firewall-cmd --reload
```
#### 3.1 查询
```shell script
firewall-cmd --query-port=8888/tcp
```
### 4. 应用场景