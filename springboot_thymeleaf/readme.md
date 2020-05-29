# springboot炖thymeleaf
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
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
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
    public String test(Model model) {
        model.addAttribute("msg", "Oven");
        return "index";
    }

}
```
#### 2.8 编写配置文件
```properties
spring.thymeleaf.cache=false
```
#### 2.9 在src/main/resources/templates下编写index.html文件
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" type="text/css" th:href="@{/css/index.css}">
    <script type="application/javascript" th:src="@{/js/index.js}"></script>
</head>
<body>
<h1 th:text="${msg}"></h1>
</body>
</html>
```
#### 2.10 在src/main/resources/static/css下编写index.css文件
```css
h1 {
    border: 1px solid red;
}
```
#### 2.11 在src/main/resources/static/js下编写index.js文件
```js
alert("Oven");
```
#### 2.12 编译打包运行
### 3. 应用场景