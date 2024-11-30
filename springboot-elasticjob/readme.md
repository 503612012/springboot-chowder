# springboot炖elasticjob

### 1. 先睹为快

### 2. docker安装zookeeper

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
        <groupId>org.apache.shardingsphere.elasticjob</groupId>
        <artifactId>elasticjob-lite-spring-boot-starter</artifactId>
        <version>3.0.0</version>
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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

#### 3.7 编辑配置文件

```properties
elasticjob.reg-center.server-lists=xxx.xxx.xxx.xxx:2181
elasticjob.reg-center.namespace=springboot-elasticjob
elasticjob.jobs.my-simple-job.elastic-job-class=com.oven.job.MySimpleJob
elasticjob.jobs.my-simple-job.cron=0/5 * * * * ?
elasticjob.jobs.my-simple-job.sharding-total-count=1
```

#### 3.8 编译打包运行

### 4. 应用场景