# springboot炖freemarker
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
#### 2.4 pom.xml文件中加入web和freemarker依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-start4er-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-freemarker</artifactId>
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
#### 2.7 开发测试接口类
```java
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping("/test")
    public String index(Model model) {
        model.addAttribute("msg", "Oven");
        return "index";
    }

}
```
#### 2.8 编写配置文件
```properties
server.port=8080

# 设置ftl文件路径
spring.freemarker.template-loader-path=classpath:/templates
# 设置静态文件（css、js、图片等）路径
spring.mvc.static-path-pattern=/static/**
```
#### 2.9 编写页面文件index.ftl
```ftl
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link type="text/css" rel="stylesheet" href="/static/css/index.css">
    <script type="application/javascript" src="/static/js/index.js"></script>
</head>
<body>
<h1>${msg}</h1>
</body>
</html>
```
#### 2.10 编写css文件
```css
h1 {
    border: 2px solid red;
}
```
#### 2.11 编写js文件
```js
alert("Oven");
```
#### 2.12 编译打包运行
### 3. 应用场景