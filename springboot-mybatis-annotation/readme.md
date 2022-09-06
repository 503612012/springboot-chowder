# springboot炖mybatis-annotation
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
#### 2.4 pom.xml文件中加入相关依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.3.2</version>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <dependency>
        <groupId>com.github.pagehelper</groupId>
        <artifactId>pagehelper-spring-boot-starter</artifactId>
        <version>1.2.7</version>
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
import lombok.Data;

@Data
public class User {

    private Integer id;
    private String uname;
    private String pwd;
    private Integer age;

}
```
#### 2.8 开发UserMapper类
```java
import com.github.pagehelper.Page;
import com.oven.vo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("insert into t_user (dbid, uname, pwd, age) value (null, #{uname}, #{pwd}, #{age})")
    void add(User user);

    @Delete("delete from t_user where dbid = #{id}")
    void delete(Integer id);

    @Update("update t_user set uname=#{uname}, pwd=#{pwd}, age=#{age} where dbid=#{id}")
    void update(User user);

    @Select("select * from t_user where dbid = #{id}")
    @Results(value = {
            @Result(column = "dbid", property = "id")
    })
    User getById(Integer id);

    @Select("select * from t_user where uname = #{uname}")
    @Results(value = {
            @Result(column = "dbid", property = "id")
    })
    User getByUname(String uname);

    @Select("select * from t_user")
    @Results(value = {
            @Result(column = "dbid", property = "id")
    })
    Page<User> getByPage();

}
```
#### 2.9 开发UserService类
```java
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.oven.mapper.UserMapper;
import com.oven.vo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public void add(User user) {
        userMapper.add(user);
    }

    public void delete(Integer id) {
        userMapper.delete(id);
    }

    public void update(User user) {
        userMapper.update(user);
    }

    public User getById(Integer id) {
        return userMapper.getById(id);
    }

    public User getByUname(String uname) {
        return userMapper.getByUname(uname);
    }

    public Page<User> getByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return userMapper.getByPage();
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

    @RequestMapping("/getByPage")
    public Object getByPage(Integer pageNum, Integer pageSize) {
        return userService.getByPage(pageNum, pageSize);
    }

}
```
#### 2.11 编写配置文件
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_test?useUnicode=true&characterEncoding=utf-8
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
pagehelper.helper-dialect=mysql
pagehelper.reasonable=true
pagehelper.support-methods-arguments=true
```
#### 2.12 编译打包运行
### 3. 应用场景