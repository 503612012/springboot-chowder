# springboot炖mongodb
### 1. 先睹为快
### 2. 实现原理
#### 2.1 docker中安装mongodb环境
##### 2.1.1 拉取镜像
```shell script
docker pull mongo
```
##### 2.1.2 启动容器
```shell script
docker run --name my-mongo -p 27017:27017 -d mongo
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
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
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
#### 2.2.6 开发用户实体类
```java
import lombok.Data;

@Data
public class User {

    private Integer id;
    private String name;
    private Integer age;
    private String pwd;

}
```
#### 2.2.7 开发用户Dao层
```java
import com.oven.vo.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDao extends MongoRepository<User, Integer> {
}
```
#### 2.2.8 开发用户service层
```java
import com.oven.dao.UserDao;
import com.oven.vo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    public void save(User user) {
        userDao.save(user);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public void delete(Integer id) {
        userDao.deleteById(id);
    }

}
```
#### 2.2.9 开发测试控制层
```java
import com.oven.service.UserService;
import com.oven.vo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DemoController {

    @Resource
    private UserService userService;

    @RequestMapping("/add")
    public String add(User user) {
        userService.save(user);
        return "添加成功";
    }

    @RequestMapping("/findAll")
    public List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping("/delete")
    public String delete(Integer id) {
        userService.delete(id);
        return "删除成功";
    }

}
```
#### 2.2.10 编写配置文件
```yaml
spring:
  data:
    mongodb:
      host: 172.16.188.194
      port: 27017
      database: springboot_mongo
```
#### 2.3 编译打包运行
### 3. 应用场景