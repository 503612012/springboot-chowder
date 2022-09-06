# springboot炖shiro
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

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>

    <dependency>
        <groupId>org.apache.shiro</groupId>
        <artifactId>shiro-spring-boot-web-starter</artifactId>
        <version>1.5.2</version>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <dependency>
        <groupId>com.github.theborakompanioni</groupId>
        <artifactId>thymeleaf-extras-shiro</artifactId>
        <version>2.0.0</version>
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
#### 2.7 开发用户实体类
```java
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private Integer id;
    private String userName;
    private String password;
    private Integer age;

}
```
#### 2.8 开发用户服务层
```java
import com.oven.vo.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User findByUserName(String userName) {
        return new User(1, userName, "123456", 18);
    }

}
```
#### 2.9 开发角色服务层
```java
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    public List<String> findByUserId(Integer id) {
        System.out.println("此处省略查询数据库过程" + id);
        List<String> result = new ArrayList<>();
        result.add("超级管理员");
        result.add("普通用户");
        return result;
    }

}
```
#### 2.10 开发角色权限服务层
```java
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleMenuService {

    public List<String> findByUserId(Integer id) {
        System.out.println("此处省略查询数据库过程" + id);
        List<String> result = new ArrayList<>();
        result.add("user:list");
        result.add("user:add");
        result.add("user:update");
        return result;
    }

}
```
#### 2.11 开发自定义realm
```java
import com.oven.service.RoleMenuService;
import com.oven.service.RoleService;
import com.oven.service.UserService;
import com.oven.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private RoleMenuService roleMenuService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        List<String> rolesSet = roleService.findByUserId(user.getId());
        List<String> permsSet = roleMenuService.findByUserId(user.getId());

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(new HashSet<>(rolesSet));
        info.setStringPermissions(new HashSet<>(permsSet));
        return info;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());
        User user = userService.findByUserName(userName);
        if (user == null) {
            throw new UnknownAccountException("账户不存在！");
        }
        if (!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException("密码错误！");
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }

}
```
#### 2.12 开发shiro配置类
```java
import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public MyShiroRealm myShiroRealm() {
        return new MyShiroRealm();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/");
        shiroFilterFactoryBean.setUnauthorizedUrl("/noauth");

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/file/**", "anon");

        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/noauth", "anon");
        filterChainDefinitionMap.put("/doLogin", "anon");
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SessionsSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        long time = 2 * 60 * 60 * 1000;
        sessionManager.setGlobalSessionTimeout(time);
        return sessionManager;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

}
```
#### 2.13 开发自定义异常处理类
```java
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NoPermissionException {

    @ExceptionHandler(UnauthorizedException.class)
    public String handleShiroException(Exception e) {
        e.printStackTrace();
        return "/noauth";
    }

    @ExceptionHandler(AuthorizationException.class)
    public String AuthorizationException(Exception e) {
        e.printStackTrace();
        return "/noauth";
    }

}
```
#### 2.14 开发用户控制层
```java
import com.oven.vo.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/list")
    @RequiresPermissions("user:list")
    public List<User> list() {
        List<User> list = new ArrayList<>();
        list.add(new User(1, "Oven", "123456", 18));
        list.add(new User(2, "Oven", "123456", 18));
        return list;
    }

    @RequestMapping("/add")
    @RequiresPermissions("user:add")
    public String add() {
        return "添加成功";
    }

    @RequestMapping("/update")
    @RequiresPermissions("user:update")
    public String update() {
        return "修改成功";
    }

    @RequestMapping("/delete")
    @RequiresPermissions("user:delete")
    public String delete() {
        return "删除成功";
    }

}
```
#### 2.15 开发测试控制层
```java
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/noauth")
    public String noauth() {
        return "noauth";
    }

    @RequestMapping("/doLogin")
    public String doLogin(String userName, String password) {
        try {
            System.out.println(userName + "-" + password);
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            subject.login(token);
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
```
#### 2.16 开发登录页面
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>login</title>
</head>
<body>
<form action="/doLogin" method="post">
    <input type="text" name="userName">
    <input type="password" name="password">
    <input type="submit" value="登录">
</form>
</body>
</html>
```
#### 2.17 开发无权限页面
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>noauth</title>
</head>
<body>
<h1>noauth</h1>
</body>
</html>
```
#### 2.18 开发系统主页
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org" xmlns:shiro="http://www.pollix.at/thymeleaf/shiro">
<head>
    <meta charset="UTF-8">
    <title>index</title>
</head>
<body>
<h1>index</h1>
<h1 shiro:hasPermission="user:list">user:list</h1>
<h1 shiro:hasPermission="user:add">user:add</h1>
<h1 shiro:hasPermission="user:update">user:update</h1>
<h1 shiro:hasPermission="user:delete">user:delete</h1>
</body>
</html>
```
#### 2.19 编译打包运行
### 3. 应用场景