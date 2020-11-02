# springboot炖i18n
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
        <groupId>org.webjars</groupId>
        <artifactId>webjars-locator-core</artifactId>
    </dependency>

    <dependency>
        <groupId>org.webjars</groupId>
        <artifactId>jquery</artifactId>
        <version>3.3.1</version>
    </dependency>

    <dependency>
        <groupId>org.webjars.bower</groupId>
        <artifactId>jquery-i18n-properties</artifactId>
        <version>1.2.7</version>
    </dependency>

    <dependency>
        <groupId>org.webjars.npm</groupId>
        <artifactId>mdui</artifactId>
        <version>0.4.0</version>
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
#### 2.7 开发i18n配置类
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * 配置国际化语言
 */
@Configuration
public class LocaleConfig {

    /**
     * 默认解析器 其中locale表示默认语言
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    /**
     * 默认拦截器 其中lang表示切换语言的参数名
     */
    @Bean
    public WebMvcConfigurer localeInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
                localeInterceptor.setParamName("lang");
                registry.addInterceptor(localeInterceptor);
            }
        };
    }
    
}
```
#### 2.8 开发测试控制器类
```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/")
    public String index() {
        return "login";
    }

}
```
#### 2.9 开发配置文件
```java
spring:
  messages:
    basename: static/i18n/messages
```
#### 2.10 开发i18n配置文件-默认
```properties
user.title=
```
#### 2.11 开发i18n配置文件-en_US
```properties
user.title=User Login
user.welcome=Welcome
user.username=Username
user.password=Password
user.login=Sign In
```
#### 2.12 开发i18n配置文件-zh_CN
```properties
user.title=用户登陆
user.welcome=欢迎
user.username=登陆用户
user.password=登陆密码
user.login=登陆
```
#### 2.13 开发i18n配置文件-zh_TW
```properties
user.title=用戶登陸
user.welcome=歡迎
user.username=登陸用戶
user.password=登陸密碼
user.login=登陸
```
#### 2.14 开发登录页面
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">
<head>
    <title th:text="#{user.title}">用户登陆</title>
    <script th:src="@{/webjars/jquery/3.3.1/jquery.min.js}"></script>
    <script th:src="@{/webjars/jquery-i18n-properties/jquery.i18n.properties.min.js}"></script>

    <script th:inline="javascript">
        // 获取应用路径
        const ROOT = [[${#servletContext.contextPath}]];

        // 获取默认语言
        const LANG_COUNTRY = [[${#locale.language+'_'+#locale.country}]];

        // 初始化i18n插件
        $.i18n.properties({
            path: ROOT + '/i18n/',
            name: 'messages',
            language: LANG_COUNTRY,
            mode: 'both'
        });

        // 初始化i18n函数
        // noinspection JSUnusedGlobalSymbols
        function i18n(msgKey) {
            try {
                return $.i18n.prop(msgKey);
            } catch (e) {
                return msgKey;
            }
        }
    </script>
</head>
<body>
<div>
    <select id="locale">
        <option value="zh_CN">中文简体</option>
        <option value="zh_TW">中文繁体</option>
        <option value="en_US">English</option>
    </select>
    <h3 th:text="#{user.welcome}">欢迎登陆</h3>

    <form>
        <div>
            <span></span>
            <input id="username" name="username" type="text" autocomplete="new-password" th:placeholder="#{user.username}">
        </div>
        <div>
            <span></span>
            <input id="password" name="password" type="password" autocomplete="new-password" th:placeholder="#{user.password}">
        </div>
        <div>
            <button th:text="#{user.login}">登录</button>
        </div>
    </form>
</div>

<!--suppress JSJQueryEfficiency -->
<script th:inline="javascript">
    //选中语言
    $("#locale").find('option[value="' + LANG_COUNTRY + '"]').attr('selected', true);

    //切换语言
    $("#locale").change(function() {
        $.get(ROOT + '/?lang=' + $("#locale").val(), function() {
            location.reload();
        });
    });
</script>

</body>
</html>
```
#### 2.15 编译打包运行
### 3. 应用场景