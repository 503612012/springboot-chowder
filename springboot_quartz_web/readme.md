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
#### 2.7 开发任务类1
```java
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class MyJob1 extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        System.out.println("任务111开始执行。。。" + new DateTime().toString("HH:mm:ss"));
    }

}
```
#### 2.8 开发任务类2
```java
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class MyJob2 extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        System.out.println("任务222开始执行。。。" + new DateTime().toString("HH:mm:ss"));
    }

}
```
#### 2.9 开发quartz配置类
```java
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.oven.quartz.job.MyJob1;
import com.oven.quartz.job.MyJob2;
import org.quartz.*;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Date;

@Configuration
public class QuartzSchedulerConfig {

    @Resource
    private Scheduler scheduler;

    /**
     * 开始执行所有任务
     */
    public void startAllJob() throws SchedulerException {
        startJob1(scheduler);
        startJob2(scheduler);
        scheduler.start();
    }

    /**
     * 获取job信息
     */
    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s, state:%s", cronTrigger.getCronExpression(), scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * 修改某个任务的执行时间
     */
    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder)
                    .build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     */
    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     */
    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     */
    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.deleteJob(jobKey);
    }

    private void startJob1(Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(MyJob1.class).withIdentity("job1", "group1").build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/2 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("job1", "group1")
                .withSchedule(cronScheduleBuilder)
                .build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    private void startJob2(Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(MyJob2.class).withIdentity("job2", "group2").build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("job2", "group2")
                .withSchedule(cronScheduleBuilder)
                .build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.quartz.properties.org.quartz.datasource")
    public DataSource quartzDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

}
```
#### 2.10 开发测试接口
```java
import com.oven.quartz.config.QuartzSchedulerConfig;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/quartz")
public class DemoController {

    @Resource
    private QuartzSchedulerConfig quartzSchedulerConfig;

    @RequestMapping("/startAll")
    public String startAllJob() {
        try {
            quartzSchedulerConfig.startAllJob();
            return "启动成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "启动失败";
        }
    }

    @RequestMapping("/getInfo")
    public String getJobInfo(String name, String group) {
        String info;
        try {
            info = quartzSchedulerConfig.getJobInfo(name, group);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return null;
        }
        return info;
    }

    @RequestMapping("/modify")
    public String modifyJob(String name, String group, String time) {
        boolean flag;
        try {
            flag = quartzSchedulerConfig.modifyJob(name, group, time);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "修改失败";
        }
        if (flag) {
            return "修改成功";
        }
        return "修改失败";
    }

    @RequestMapping(value = "/pause")
    public String pauseJob(String name, String group) {
        try {
            quartzSchedulerConfig.pauseJob(name, group);
            return "暂停成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "暂停失败";
        }
    }

    @RequestMapping(value = "/pauseAll")
    public String pauseAllJob() {
        try {
            quartzSchedulerConfig.pauseAllJob();
            return "暂停全部成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "暂停全部失败";
        }
    }


    @RequestMapping(value = "/resume")
    public String resume(String name, String group) {
        try {
            quartzSchedulerConfig.resumeJob(name, group);
            return "重启成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "重启失败";
        }
    }

    @RequestMapping(value = "/resumeAll")
    public String resumeAll() {
        try {
            quartzSchedulerConfig.resumeAllJob();
            return "重启全部成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "重启全部失败";
        }
    }

    @RequestMapping(value = "/delete")
    public String delete(String name, String group) {
        try {
            quartzSchedulerConfig.deleteJob(name, group);
            return "删除成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "删除失败";
        }
    }

}
```
#### 2.11 编写配置文件
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
#### 2.12 编译打包运行
### 3. 应用场景