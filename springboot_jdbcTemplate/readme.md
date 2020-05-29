# springboot炖jdbcTemplate
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
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
        
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
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
#### 2.8 开发UserDao类
```java
import com.oven.vo.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void add(User user) {
        jdbcTemplate.update("insert into t_user (dbid, uname, pwd, age) value (null, ?, ?, ?)", user.getUname(), user.getPwd(), user.getAge());
    }

    public void delete(Integer id) {
        jdbcTemplate.update("delete from t_user where dbid = ?", id);
    }

    public void update(User user) {
        jdbcTemplate.update("update t_user set uname=?, pwd=?, age=? where dbid=?", user.getUname(), user.getPwd(), user.getAge(), user.getId());
    }

    public User getById(Integer id) {
        return jdbcTemplate.queryForObject("select * from t_user where dbid=?", userRowMapper(), id);
    }

    public List<User> get() {
        return jdbcTemplate.query("select * from t_user", userRowMapper());
    }

    private RowMapper<User> userRowMapper() {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("dbid"));
                user.setUname(rs.getString("uname"));
                user.setPwd(rs.getString("pwd"));
                user.setAge(rs.getInt("age"));
                return user;
            }
        };
    }

}
```
#### 2.9 开发UserService类
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

    public void add(User user) {
        userDao.add(user);
    }

    public void delete(Integer id) {
        userDao.delete(id);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public User getById(Integer id) {
        return userDao.getById(id);
    }

    public List<User> get() {
        return userDao.get();
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

    @RequestMapping("/get")
    public Object get() {
        return userService.get();
    }

}
```
#### 2.11 编写配置文件
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_test?useUnicode=true&characterEncoding=utf-8
spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
```
#### 2.12 编译打包运行
### 3. 应用场景