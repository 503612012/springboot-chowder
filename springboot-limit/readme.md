# springboot炖限流
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
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
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
#### 2.7 开发IP工具类
```java
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * IP工具类
 */
public class IPUtils {

    public static String getClientIPAddr(HttpServletRequest request) {
        String ip;
        // 1.首先考虑有反向代理的情况，如果有代理，通过“x-forwarded-for”获取真实ip地址
        ip = request.getHeader("x-forwarded-for");
        // 2.如果squid.conf的配制文件forwarded_for项默认是off，则：X-Forwarded-For：unknown。考虑用Proxy-Client-IP或WL-Proxy-Client-IP获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        // 3.最后考虑没有代理的情况，直接用request.getRemoteAddr()获取ip
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            // 使用localhost访问
            if ("0:0:0:0:0:0:0:1".equals(ip)) {
                ip = "127.0.0.1";
            }
        }
        // 4.如果通过多级反向代理，则可能产生多个ip，其中第一个非unknown的IP为客户端真实IP（IP按照','分割）
        if (ip != null && ip.split(",").length > 1) {
            ip = (ip.split(","))[0];
        }
        // 5.如果是服务器本地访问，需要根据网卡获取本机真实ip
        if ("127.0.0.1".equals(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        // 6.校验ip的合法性，不合法返回""
        if (!isValidIp(ip)) {
            return "";
        } else {
            return ip;
        }
    }

    /**
     * 判断是否为合法IP地址
     *
     * @param ipAddress ip地址
     */
    private static boolean isValidIp(String ipAddress) {
        boolean retVal = false;
        try {
            if (ipAddress != null && !"".equals(ipAddress)) {
                String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
                Pattern pattern = Pattern.compile(regex);
                retVal = pattern.matcher(ipAddress).matches();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

}
```
#### 2.8 开发redis配置类
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

@Configuration
public class RedisConfig {

    /**
     * 为限流提供redis模板
     */
    @Bean
    public RedisTemplate<String, Serializable> limitRedisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
```
#### 2.9 开发限流自定义注解
```java
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用服务端限流注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {

    /**
     * 资源的名字
     */
    String name() default "";

    /**
     * 资源的key
     */
    String key() default "";

    /**
     * Key的prefix
     */
    String prefix() default "";

    /**
     * 给定的时间段,单位秒
     */
    int period();

    /**
     * 最多的访问限制次数
     */
    int count();

    /**
     * 限流后返回的描述信息
     */
    String errMsg();

    /**
     * 类型
     */
    LimitType limitType() default LimitType.CUSTOMER;

}
```
#### 2.10 开发自定义异常类
```java
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义限流异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LimitException extends RuntimeException {

    private static final long serialVersionUID = -1920795727304163167L;

    private Integer code;
    private String msg;

    LimitException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
```
#### 2.11 开发限流枚举类
```java
public enum LimitType {

    /**
     * ip+方法名
     */
    IP_AND_METHOD,

    /**
     * 自定义key
     */
    CUSTOMER,

    /**
     * 根据请求者IP
     */
    IP

}
```
#### 2.12 开发自定义限流注解处理器
```java
import com.google.common.collect.ImmutableList;
import com.oven.util.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 使用aop拦截注解实现应用服务器分布式限流的目的,相同key共享限制次数
 */
@Slf4j
@Aspect
@Configuration
public class LimitInterceptor {

    private final RedisTemplate<String, Serializable> limitRedisTemplate;

    @Autowired
    public LimitInterceptor(RedisTemplate<String, Serializable> limitRedisTemplate) {
        this.limitRedisTemplate = limitRedisTemplate;
    }

    @Around("execution(public * *(..)) && @annotation(com.oven.limitation.Limit)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Limit limitAnnotation = method.getAnnotation(Limit.class);
        LimitType limitType = limitAnnotation.limitType();
        String key;
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();
        String errMsg = limitAnnotation.errMsg();
        switch (limitType) {
            case CUSTOMER:
                key = limitAnnotation.key();
                break;
            case IP_AND_METHOD:
                @SuppressWarnings("ConstantConditions")
                HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                key = IPUtils.getClientIPAddr(req) + "_" + limitAnnotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(limitAnnotation.prefix(), key));
        try {
            String luaScript = buildLuaScript();
            RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
            Number count = limitRedisTemplate.execute(redisScript, keys, limitCount, limitPeriod);
            if (count != null && count.intValue() <= limitCount) {
                return pjp.proceed();
            } else {
                throw new RuntimeException(errMsg);
            }
        } catch (Throwable e) {
            throw new LimitException(7001, errMsg);
        }
    }

    /**
     * 限流脚本
     *
     * @return lua脚本
     */
    public String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                // 调用不超过最大值，则直接返回
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                // 执行计算器自加
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                // 从第一次调用开始限流，设置对应键值的过期
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }

}
```
#### 2.13 开发测试控制器
```java
import com.oven.limitation.Limit;
import com.oven.limitation.LimitType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping("/limit")
    @Limit(key = "limit.key", period = 5, count = 5, errMsg = "限流异常", limitType = LimitType.IP_AND_METHOD)
    public String limit() {
        return "hello...";
    }

}
```
#### 2.14 编写配置文件
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: 5217
```
#### 2.15 编译打包运行
### 3. 应用场景