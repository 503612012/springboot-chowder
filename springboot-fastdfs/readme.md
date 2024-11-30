# springboot炖fileUpload
### 1. 先睹为快
### 2. docker安装fileUpload
#### 2.1 下载fastdfs镜像
```shell script
docker pull delron/fastdfs
```
#### 2.2 创建本地目录
```shell script
mkdir /usr/local/fastdfs/tracker
mkdir /usr/local/fastdfs/storage
```
#### 2.3 启动tracker容器
```shell script
docker run -d --network=host --name tracker -v /usr/local/fastdfs/tracker:/var/fdfs delron/fastdfs tracker
```
#### 2.4 启动stroage容器
```shell script
docker run -d --network=host --name storage -e TRACKER_SERVER=192.168.63.2:22122 -v /usr/local/fastdfs/storage:/var/fdfs -e GROUP_NAME=group1 delron/fastdfs storage
```
#### 2.5 测试
##### 2.5.1 拷贝一张图片(test.jpg)到/usr/local/fastdfs/storage目录中
##### 2.5.2 进入到storage容器中
```shell script
docker exec -it storage bash
```
##### 2.5.3 进入到fdfs目录
```shell script
cd /var/fdfs
```
##### 2.5.4 执行命令
```shell script
/usr/bin/fdfs_upload_file /etc/fdfs/client.conf test.jpg
```
##### 2.5.5 用上述命令的返回结果到浏览器访问
```http request
http://192.168.63.2:8888/group1/M00/00/00/xxxxxxxxxxxxxxxxxxxx.jpg
```
### 3. 实现原理
#### 3.1 新建项目
#### 3.2 创建maven目录结构，以及pom.xml文件
#### 3.3 pom.xml文件中加入依赖
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.5.RELEASE</version>
    <relativePath/>
</parent>
```
#### 3.4 pom.xml文件中加入springboot-starter依赖
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
        <groupId>com.github.tobato</groupId>
        <artifactId>fastdfs-client</artifactId>
        <version>1.26.6</version>
    </dependency>
</dependencies>
```
#### 3.5 pom.xml文件中加入maven-springboot打包插件
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
#### 3.6 开发启动类
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
#### 3.7 开发fastdfs工具类
```java
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

@Component
public class FastdfsUtils {

    public static final String DEFAULT_CHARSET = "UTF-8";

    @Resource
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 上传
     */
    public StorePath upload(MultipartFile file) throws IOException {
        // 设置文件信息
        Set<MetaData> mataData = new HashSet<>();
        mataData.add(new MetaData("author", "Oven"));
        mataData.add(new MetaData("description", file.getOriginalFilename()));
        // 上传
        return fastFileStorageClient.uploadFile(
                file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()),
                null);
    }

    /**
     * 删除
     */
    public void delete(String path) {
        fastFileStorageClient.deleteFile(path);
    }

    /**
     * 删除
     */
    public void delete(String group, String path) {
        fastFileStorageClient.deleteFile(group, path);
    }

    /**
     * 文件下载
     *
     * @param path     文件路径
     * @param filename 下载的文件命名
     */
    public void download(String path, String filename, HttpServletResponse response) throws IOException {
        // 获取文件
        StorePath storePath = StorePath.parseFromUrl(path);
        if (StringUtils.isBlank(filename)) {
            filename = FilenameUtils.getName(storePath.getPath());
        }
        byte[] bytes = fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        response.reset();
        response.setContentType("applicatoin/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, DEFAULT_CHARSET));
        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }

}
```
#### 3.8 开发文件上传接口
```java
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.oven.utils.FastdfsUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

@Controller
public class DemoController {

    @Resource
    private FastdfsUtils fastdfsUtils;

    @RequestMapping("/")
    public String index() {
        return "upload";
    }

    @RequestMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请选择一个文件！");
            return "redirect:/";
        }
        try {
            StorePath path = fastdfsUtils.upload(file);
            redirectAttributes.addFlashAttribute("message", "上传【" + file.getOriginalFilename() + "】成功!");
            redirectAttributes.addFlashAttribute("path", "http://192.168.63.2:8888/" + path.getFullPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

}
```
#### 3.9 开发文件上传下载页面
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>springboot炖fileUpload</title>
</head>
<body>

<div th:if="${message}">
    <h2 th:text="${message}"></h2>
</div>

<form method="post" action="/upload" enctype="multipart/form-data">
    <input type="file" name="file"/><br/><br/>
    <input type="submit" value="上传"/>
</form>

<div th:if="${path}">
    <a th:href="@{${path}}" target="_blank">
        <h2 th:text="${path}"></h2>
    </a>
</div>

</body>
</html>
```
#### 3.10 编写配置文件
```properties
fdfs.connect-timeout=600
fdfs.so-timeout=1500
fdfs.thumb-image.height=150
fdfs.thumb-image.width=150
fdfs.tracker-list=192.168.63.2:22122
fdfs.pool.jmx-enabled=false
```
#### 3.11 编译打包运行
### 4. 应用场景