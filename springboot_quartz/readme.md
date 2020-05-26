# springboot炖quartz
### 1. 先睹为快
### 2. 实现原理
#### 2.1 新建项目
#### 2.2 创建maven目录结构，以及pom.xml文件
#### 2.3 pom.xml文件中加入spring-boot-starter-parent
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

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-quartz</artifactId>
    </dependency>

    <dependency>
        <groupId>joda-time</groupId>
        <artifactId>joda-time</artifactId>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.1.10</version>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
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
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
#### 2.7 开发任务类
```java
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class MyJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        System.out.println("任务开始执行。。。" + new DateTime().toString("HH:mm:ss"));
    }

}
```
#### 2.8 开发quartz配置类
```java
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.oven.quartz.job.MyJob;
import org.quartz.*;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(MyJob.class).storeDurably().build();
    }

    @Bean
    public Trigger trigger() {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(2)
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withSchedule(builder)
                .build();
    }

    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.quartz.properties.org.quartz.datasource")
    public DataSource quartzDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

}
```
#### 2.9 编写配置文件
```yaml
spring:
  quartz:
    job-store-type: jdbc
    properties:
      org:
        quartz:
          datasource:
            driver-class-name: com.mysql.jdbc.Driver
            url: jdbc:mysql://localhost:3306/db_test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
            username: root
            password: root
          scheduler:
            instancName: clusteredScheduler
            instanceId: AUTO
          jobStore:
            class: org.quartz.impl.jdbcjobstore.JobStoreTX
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_
            isClustered: true
            clusterCheckinInterval: 1000
            useProperties: false
          threadPool:
            class: org.quartz.simpl.SimpleThreadPool
            threadCount: 20
            threadPriority: 5
```
#### 2.10 初始化quartz数据库（quratz.sql）
#### 2.11 编译打包运行
### 3. 应用场景