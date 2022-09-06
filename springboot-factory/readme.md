# springboot炖factory
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
public interface IService {

    String serviceId();

    String doSomething();

}
```
#### 2.8 开发接口实现类1
```java
import com.oven.service.IService;
import org.springframework.stereotype.Service;

@Service
public class Service01Impl implements IService {

    @Override
    public String serviceId() {
        return "01";
    }

    @Override
    public String doSomething() {
        return "hello 01";
    }

}
```
#### 2.9 开发接口实现类2
```java
import com.oven.service.IService;
import org.springframework.stereotype.Service;

@Service
public class Service02Impl implements IService {

    @Override
    public String serviceId() {
        return "02";
    }

    @Override
    public String doSomething() {
        return "hello 02";
    }

}
```
#### 2.10 开发接口实现类3
```java
import com.oven.service.IService;
import org.springframework.stereotype.Service;

@Service
public class Service03Impl implements IService {

    @Override
    public String serviceId() {
        return "03";
    }

    @Override
    public String doSomething() {
        return "hello 03";
    }

}
```
#### 2.11 开发接口默认实现类
```java
import com.oven.service.IService;
import org.springframework.stereotype.Service;

@Service
public class DefaultServiceImpl implements IService {

    @Override
    public String serviceId() {
        return "00";
    }

    @Override
    public String doSomething() {
        return "hello 00";
    }

}
```
#### 2.12 开发接口工厂类
```java
import com.oven.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Hashtable;
import java.util.Map;

@Component
public class ServiceContext {

    private Map<String, IService> serviceHolder;

    @Autowired
    private void setServiceHolder(IService[] services) {
        serviceHolder = new Hashtable<>();
        for (IService service : services) {
            serviceHolder.put(service.serviceId(), service);
        }
    }

    public IService getService(String serviceId) {
        if (StringUtils.isEmpty(serviceId)) {
            serviceId = "00";
        }
        return serviceHolder.get(serviceId);
    }

}
```
#### 2.13 开发测试控制器类
```java
import com.oven.context.ServiceContext;
import com.oven.service.IService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private ServiceContext serviceContext;

    @RequestMapping("/test")
    public Object test(String serviceId) {
        IService service = serviceContext.getService(serviceId);
        return service.doSomething();
    }

}
```
#### 2.14 编译打包运行
### 3. 应用场景