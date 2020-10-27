# springboot炖webservice
### 1. 先睹为快
### 2. 实现原理
#### 2.1 新建服务端项目
#### 2.1.1 创建maven目录结构，以及pom.xml文件
#### 2.1.2 pom.xml文件中加入依赖
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.5.RELEASE</version>
    <relativePath/>
</parent>
```
#### 2.1.3 pom.xml文件中加入springboot-starter依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.apache.cxf</groupId>
        <artifactId>cxf-spring-boot-starter-jaxws</artifactId>
        <version>3.2.5</version>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```
#### 2.1.4 pom.xml文件中加入maven-springboot打包插件
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
#### 2.1.5 开发启动类
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
#### 2.1.6 开发性别枚举类
```java
public enum Gender {

    MALE("男"),
    FEMALE("女");

    String value;

    Gender(String value) {
        this.value = value;
    }

}
```
#### 2.1.7 开发用户实体类
```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;
    private Gender gender;
    private int age;
    private List<String> hobby;

}
```
#### 2.1.8 开发用户接口
```java
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = "wsdl.oven.com", name = "userPortType")
public interface UserService {

    @WebMethod(operationName = "getUserByName")
    User getUserByName(@WebParam(name = "name") String name);

    @WebMethod
    List<User> getList();

    @WebMethod
    String getString(@WebParam(name = "msg") String msg);

}
```
#### 2.1.9 开发用户接口实现类
```java
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebService(
        targetNamespace = "wsdl.oven.com", // wsdl命名空间
        name = "userPortType",                                   // portType名称，客户端生成代码时，为接口名称
        serviceName = "userService",                             // 服务name名称
        portName = "userPortName",                               // port名称
        endpointInterface = "com.oven.service.UserService"       // 指定发布webservcie的接口类，此类也需要接入@WebService注解
)
public class UserServiceImpl implements UserService {

    @Override
    public User getUserByName(String name) {
        User user = new User();
        user.setName(name);
        user.setAge(18);
        user.setGender(Gender.MALE);
        user.setHobby(Arrays.asList("吃饭", "睡觉"));
        return user;
    }

    @Override
    public List<User> getList() {
        List<User> result = new ArrayList<>();
        User user = new User();
        user.setName("Oven");
        user.setAge(18);
        user.setGender(Gender.MALE);
        user.setHobby(Arrays.asList("吃饭", "睡觉"));
        result.add(user);
        result.add(user);
        return result;
    }

    @Override
    public String getString(String msg) {
        return "hello " + msg;
    }

}
```
#### 2.1.10 开发webservice配置类
```java
import com.oven.service.UserService;
import com.oven.service.impl.UserServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class CxfWebServiceConfig {

    @Bean("cxfServletRegistration")
    public ServletRegistrationBean<CXFServlet> dispatcherServlet() {
        return new ServletRegistrationBean<>(new CXFServlet(), "/ws/*");
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public Endpoint endpoint(UserService userService) {
        EndpointImpl endpoint = new EndpointImpl(springBus(), userService);
        endpoint.publish("/user");
        return endpoint;
    }

}
```
#### 2.1.11 编译打包运行
### 2.2 新建客户端项目
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
        <groupId>org.apache.cxf</groupId>
        <artifactId>cxf-spring-boot-starter-jaxws</artifactId>
        <version>3.2.5</version>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```
#### 2.2.4 pom.xml文件中加入maven-springboot打包插件以及wsdl2java插件
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-codegen-plugin</artifactId>
            <version>3.2.5</version>
            <executions>
                <execution>
                    <id>generate-sources</id>
                    <phase>generate-sources</phase>
                    <configuration>
                        <sourceRoot>src/main/resources/cxf</sourceRoot>
                        <wsdlOptions>
                            <wsdlOption>
                                <wsdl>http://localhost:8080/ws/user?wsdl</wsdl>
                            </wsdlOption>
                        </wsdlOptions>
                    </configuration>
                    <goals>
                        <goal>wsdl2java</goal>
                    </goals>
                </execution>
            </executions>
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
#### 2.2.6 在项目根目录运行wsdl2java插件的命令，生成wsdl的java代码
```shell script
mvn generate-sources
```
#### 2.2.7 将生成的java代码复制到src/main/java/com/oven/wsdl目录中
#### 2.2.8 开发客户端配置类
```java
import com.oven.wsdl.UserPortType;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfClientConfig {

    private final static String SERVICE_ADDRESS = "http://localhost:8080/ws/user";

    @Bean("cxfProxy")
    public UserPortType createUserPortTypeProxy() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setServiceClass(UserPortType.class);
        jaxWsProxyFactoryBean.setAddress(SERVICE_ADDRESS);

        return (UserPortType) jaxWsProxyFactoryBean.create();
    }

}
```
#### 2.2.9 开发测试控制器
```java
import com.oven.wsdl.User;
import com.oven.wsdl.UserPortType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DemoController {

    @Resource(name = "cxfProxy")
    private UserPortType userPortType;

    @GetMapping("/getString")
    public String getString(String msg) {
        return userPortType.getString(msg);
    }

    @GetMapping("/getUserByName")
    public User getUserByName(String name) {
        return userPortType.getUserByName(name);
    }

    @GetMapping("/getList")
    public List<User> getList() {
        return userPortType.getList();
    }

}
```
#### 2.2.10 编写配置文件
```yaml
server:
  port: 8081
```
#### 2.2.11 编译打包运行
### 3. 应用场景