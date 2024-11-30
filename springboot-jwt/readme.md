# springboot炖jwt
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
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt</artifactId>
        <version>0.9.1</version>
    </dependency>

    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.83</version>
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
import lombok.Data;

@Data
public class User {

    private Integer id;
    private String userName;
    private String password;

}
```
#### 2.8 开发JWT工具类
```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * JwtUtils
 */
@Slf4j
public final class JwtUtils {

    /**
     * 加密的secret
     */
    private static final String secret = "OvenSecret";

    /**
     * 生成token
     */
    public static String generateToken(Map<String, Object> map) {
        if (Objects.isNull(map)) {
            map = new HashMap<>();
        }
        long expire = 15 * 60;
        Date expireDate = new Date(System.currentTimeMillis() + expire * 1000); // 过期时间
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")   // 设置头部信息
                .setClaims(map)                 // 装入自定义的用户信息
                .setExpiration(expireDate)      // token过期时间
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 校验token并解析token
     *
     * @return Claims：它继承了Map,而且里面存放了生成token时放入的用户信息
     */
    public static Claims verifyAndGetClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getHeaderKey() {
        return "token";
    }

}
```
#### 2.9 开发返回值类
```java
public class ResultInfo<T> {

    private Integer code; // 返回代码(200:成功)
    private T data; // 返回的数据,正确的信息或错误描述信息

    public ResultInfo() {
        super();
    }

    public ResultInfo(Integer code, T data) {
        super();
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }

}
```
#### 2.10 开发常量类
```java
public class AppConst {

    public static final String USER_INFO_KEY = "user_info_key";

}
```
#### 2.11 开发jwt拦截器
```java
import com.alibaba.fastjson.JSON;
import com.oven.constants.AppConst;
import com.oven.utils.JwtUtils;
import com.oven.utils.ResultInfo;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwtToken校验拦截器
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(JwtUtils.getHeaderKey()); // 获取用户token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(JwtUtils.getHeaderKey());
        }
        if (StringUtils.isBlank(token)) { // token为空
            this.writerErrorMsg(302, JwtUtils.getHeaderKey() + " can not be blank", response);
            return false;
        }
        // 校验并解析token，如果token过期或者篡改，则会返回null
        Claims claims = JwtUtils.verifyAndGetClaimsByToken(token);
        if (null == claims) {
            this.writerErrorMsg(303, JwtUtils.getHeaderKey() + "失效，请重新登录", response);
            return false;
        }
        // 校验通过后，设置用户信息到request里，在controller中从request域中获取用户信息
        request.setAttribute(AppConst.USER_INFO_KEY, claims);
        return true;
    }

    /**
     * 利用response直接输出错误信息
     */
    private void writerErrorMsg(Integer code, String msg, HttpServletResponse response) throws IOException {
        ResultInfo<String> result = new ResultInfo<>();
        result.setCode(code);
        result.setData(msg);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }

}
```
#### 2.12 开发拦截器配置类
```java
import com.oven.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(jwtInterceptor());
        registration.addPathPatterns("/jwt/**").excludePathPatterns("/jwt/login");
    }

}
```
#### 2.13 开发测试控制器类
```java
import com.oven.constants.AppConst;
import com.oven.entity.User;
import com.oven.utils.JwtUtils;
import com.oven.utils.ResultInfo;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("jwt")
public class DemoController {

    @PostMapping("login")
    public ResultInfo<String> login(@RequestBody User user) {
        ResultInfo<String> result = new ResultInfo<>();
        if ("Oven".equals(user.getUserName()) && "123456".equals(user.getPassword())) {
            Map<String, Object> map = new HashMap<>();
            map.put("userName", "Oven");
            String token = JwtUtils.generateToken(map);
            result.setData(token);
        } else {
            result.setCode(301);
            result.setData("用户名或密码错误");
        }
        return result;
    }

    @GetMapping("test")
    public String test(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute(AppConst.USER_INFO_KEY);
        if (null != claims) {
            return (String) claims.get("userName");
        } else {
            return "System Error!";
        }
    }

}
```
#### 2.14 编译打包运行
### 3. 应用场景