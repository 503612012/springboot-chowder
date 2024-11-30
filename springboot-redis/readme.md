# springboot炖redis
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
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.83</version>
    </dependency>

    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-pool2</artifactId>
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
#### 2.7 开发redis配置类
```java
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * redis配置类
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory); // 创建redis连接

        GenericFastJsonRedisSerializer jsonRedisSerializer = new GenericFastJsonRedisSerializer(); // 序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer); // key的序列化方式是string类型
        redisTemplate.setValueSerializer(jsonRedisSerializer); // value的序列化方式是 json
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);

        return redisTemplate;
    }

}
```
#### 2.8 开发redis服务类
```java
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 查询
     *
     * @param key 键 不可为空
     */
    public <T> T get(String key) {
        T obj = null;
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            //noinspection unchecked
            obj = (T) operations.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 设置
     *
     * @param key 键 不可为空
     * @param obj 值 不可为空
     */
    public <T> void set(String key, T obj) {
        set(key, obj, null);
    }

    /**
     * 设置键值，这会直接覆盖掉给定键之前映射的值
     *
     * @param key        键 不可为空
     * @param obj        值 不可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     */
    public <T> void set(String key, T obj, Long expireTime) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        if (obj == null) {
            return;
        }
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, obj);
        if (null != expireTime) {
            redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 移除
     *
     * @param key 键 不可为空
     */
    public void remove(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        redisTemplate.delete(key);
    }

    /**
     * 是否存在
     *
     * @param key 键 不可为空
     */
    public boolean contains(String key) {
        boolean exists = false;
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        Object obj = get(key);
        if (obj != null) {
            exists = true;
        }
        return exists;
    }

}
```
#### 2.9 开发用户实体类
```java
import lombok.Data;

@Data
public class User {

    private int id;
    private String name;
    private int age;
    private double score;

}
```
#### 2.10 开发测试控制器类
```java
import com.oven.entity.User;
import com.oven.service.RedisService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private RedisService redisService;

    @RequestMapping("/redis")
    public String redis() {
        System.out.println((String) redisService.get("name"));
        redisService.set("name", "Oven");
        System.out.println((String) redisService.get("name"));
        redisService.remove("name");
        System.out.println((String) redisService.get("name"));

        System.out.println((Integer) redisService.get("age"));
        redisService.set("age", 18);
        System.out.println((Integer) redisService.get("age"));
        redisService.remove("age");
        System.out.println((Integer) redisService.get("age"));

        System.out.println(redisService.contains("redis"));
        redisService.set("redis", "redis");
        System.out.println(redisService.contains("redis"));

        User user = new User();
        user.setName("Oven");
        user.setAge(18);
        user.setScore(88.88);

        System.out.println((User) redisService.get("user"));
        redisService.set("user", user);
        System.out.println((User) redisService.get("user"));
        redisService.remove("user");
        System.out.println((User) redisService.get("user"));

        return "测试完毕";
    }

}
```
#### 2.11 编译打包运行
### 3. 应用场景