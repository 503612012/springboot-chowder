# springboot炖fileUpload
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
#### 2.7 开发文件服务类
```java
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileUtils {

    private final Path path = Paths.get("/Users/oven/Desktop/");

    public void upload(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                System.out.println("文件为空！");
                return;
            }
            Files.copy(file.getInputStream(), path.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stream<Path> list() {
        try {
            return Files.walk(this.path, 1)
                    .filter(path -> !path.equals(this.path))
                    .map(this.path::relativize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = path.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                System.out.println("读取文件失败！");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
```
#### 2.8 开发文件上传下载接口
```java
import com.oven.utils.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
public class DemoController {

    @javax.annotation.Resource
    private FileUtils fileUtils;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("files", fileUtils
                .list()
                .map(path -> MvcUriComponentsBuilder
                        .fromMethodName(DemoController.class, "download", path.getFileName().toString())
                        .build()
                        .toString())
                .collect(Collectors.toList()));
        return "upload";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        Resource file = fileUtils.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (StringUtils.isEmpty(file.getOriginalFilename())) {
            redirectAttributes.addFlashAttribute("message", "请选择文件!");
            return "redirect:/";
        }
        fileUtils.upload(file);
        redirectAttributes.addFlashAttribute("message", "上传【" + file.getOriginalFilename() + "】成功!");
        return "redirect:/";
    }

}
```
#### 2.9 开发文件上传下载页面
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="en">
<body>

<div th:if="${message}">
    <h2 th:text="${message}"></h2>
</div>

<div>
    <form method="POST" enctype="multipart/form-data" action="/">
        <table>
            <tr>
                <td><input type="file" name="file"/></td>
            </tr>
            <tr>
                <td><input type="submit" value="上传"/></td>
            </tr>
        </table>
    </form>
</div>

<div>
    <ul>
        <li th:each="file : ${files}">
            <a th:href="${file}" th:text="${file}"></a>
        </li>
    </ul>
</div>

</body>
</html>
```
#### 2.10 编译打包运行
### 3. 应用场景