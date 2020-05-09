# springboot炖hibernate
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
#### 2.4 pom.xml文件中加入相关依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
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
#### 2.7 开发User类
```java
@Entity
@Table(name = "t_user")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer"})
public class User {

    @Id
    @Column(name = "dbid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "uname")
    private String uname;
    @Column(name = "pwd")
    private String pwd;
    @Column(name = "age")
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
```
#### 2.8 开发UserRepository类
```java
import com.oven.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User getByUname(String uname);

}
```
#### 2.9 开发UserService类
```java
import com.oven.dao.UserRepository;
import com.oven.vo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    public void add(User user) {
        userRepository.save(user);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public void update(User user) {
        User userInDb = userRepository.getOne(user.getId());
        userInDb.setUname(user.getUname());
        userInDb.setPwd(user.getPwd());
        userInDb.setAge(user.getAge());
        userRepository.deleteById(user.getId());
        userRepository.save(userInDb);
    }

    public User getById(Integer id) {
        return userRepository.getOne(id);
    }

    public User getByUname(String uname) {
        return userRepository.getByUname(uname);
    }

}
```
#### 2.10 开发测试接口类
```java
import com.oven.service.UserService;
import com.oven.vo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private UserService userService;

    @RequestMapping("/add")
    public Object add(User user) {
        userService.add(user);
        return "添加成功";
    }

    @RequestMapping("/delete")
    public Object delete(Integer id) {
        userService.delete(id);
        return "删除成功";
    }

    @RequestMapping("/update")
    public Object update(User user) {
        userService.update(user);
        return "修改成功";
    }

    @RequestMapping("/getById")
    public Object getById(Integer id) {
        return userService.getById(id);
    }

    @RequestMapping("/getByUname")
    public Object getByUname(String uname) {
        return userService.getByUname(uname);
    }

}
```
#### 2.11 编写配置文件
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_test?useUnicode=true&characterEncoding=utf-8
spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.show-sql=true
```
#### 2.12 编译打包运行
### 3. 应用场景