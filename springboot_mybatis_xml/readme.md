# springboot炖mybatis_xml
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
import com.oven.vo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void add(User user);

    void delete(Integer id);

    void update(User user);

    User getById(Integer id);

    User getByUname(String uname);

}
```
#### 2.9 开发UserMapper映射文件 
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.oven.mapper.UserMapper">
    
    <insert id="add">
        insert into t_user (dbid, uname, pwd, age) value (null, #{uname}, #{pwd}, #{age})
    </insert>
    
    <update id="update">
        update t_user set uname=#{uname}, pwd=#{pwd}, age=#{age} where dbid=#{id}
    </update>
    
    <delete id="delete">
        delete from t_user where dbid = #{id}
    </delete>
    
    <select id="getById" resultMap="userMap">
        select * from t_user where dbid = #{id}
    </select>
    
    <select id="getByUname" resultMap="userMap">
        select * from t_user where uname = #{uname}
    </select>
    
    <resultMap id="userMap" type="com.oven.vo.User">
        <id column="dbid" property="id"/>
        <result column="uname" property="uname"/>
        <result column="pwd" property="pwd"/>
        <result column="age" property="age"/>
    </resultMap>
    
</mapper>
```
#### 2.10 开发UserService类
```java
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

}
```
#### 2.11 开发测试接口类
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
#### 2.12 编写配置文件
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_test?useUnicode=true&characterEncoding=utf-8
spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
mybatis.type-aliases-package=com.oven.mapper
mybatis.mapper-locations=classpath:mapper/*.xml
```
#### 2.13 编译打包运行
### 3. 应用场景