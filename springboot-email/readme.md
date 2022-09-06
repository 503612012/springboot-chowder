# springboot炖email
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
        <artifactId>spring-boot-starter-mail</artifactId>
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
#### 2.7 开发邮件发送接口
```java
public interface MailService {

    /**
     * 发送简单文本的邮件
     */
    boolean send(String to, String subject, String content);

    /**
     * 发送 html 的邮件
     */
    boolean sendWithHtml(String to, String subject, String html);

    /**
     * 发送带有图片的 html 的邮件
     */
    boolean sendWithImageHtml(String to, String subject, String html, String[] cids, String[] filePaths);


    /**
     * 发送带有附件的邮件
     */
    boolean sendWithWithEnclosure(String to, String subject, String content, String[] filePaths);

}
```
#### 2.8 开发邮件发送实现类
```java
import com.oven.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private MailProperties mailProperties;
    @Resource
    private JavaMailSender javaMailSender;

    /**
     * 发送简单文本的邮件
     */
    @Override
    public boolean send(String to, String subject, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 邮件发送来源
        simpleMailMessage.setFrom(mailProperties.getUsername());
        // 邮件发送目标
        simpleMailMessage.setTo(to);
        // 设置标题
        simpleMailMessage.setSubject(subject);
        // 设置内容
        simpleMailMessage.setText(content);

        try {
            // 发送
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            log.error("send mail error: ", e);
            return false;
        }

        return true;
    }

    /**
     * 发送 html 的邮件
     */
    @Override
    public boolean sendWithHtml(String to, String subject, String html) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            // 邮件发送来源
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            // 邮件发送目标
            mimeMessageHelper.setTo(to);
            // 设置标题
            mimeMessageHelper.setSubject(subject);
            // 设置内容，并设置内容 html 格式为 true
            mimeMessageHelper.setText(html, true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("send html mail error: ", e);
            return false;
        }
        return true;
    }

    /**
     * 发送带有图片的 html 的邮件
     */
    @Override
    public boolean sendWithImageHtml(String to, String subject, String html, String[] cids, String[] filePaths) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            // 邮件发送来源
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            // 邮件发送目标
            mimeMessageHelper.setTo(to);
            // 设置标题
            mimeMessageHelper.setSubject(subject);
            // 设置内容，并设置内容 html 格式为 true
            mimeMessageHelper.setText(html, true);

            // 设置 html 中内联的图片
            for (int i = 0; i < cids.length; i++) {
                FileSystemResource file = new FileSystemResource(filePaths[i]);
                mimeMessageHelper.addInline(cids[i], file);
            }

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("send html mail error: ", e);
            return false;
        }
        return true;
    }

    /**
     * 发送带有附件的邮件
     */
    @Override
    public boolean sendWithWithEnclosure(String to, String subject, String content, String[] filePaths) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            // 邮件发送来源
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            // 邮件发送目标
            mimeMessageHelper.setTo(to);
            // 设置标题
            mimeMessageHelper.setSubject(subject);
            // 设置内容
            mimeMessageHelper.setText(content);

            // 添加附件
            for (int i = 0; i < filePaths.length; i++) {
                FileSystemResource file = new FileSystemResource(filePaths[i]);
                String attachementFileName = "附件" + (i + 1);
                mimeMessageHelper.addAttachment(attachementFileName, file);
            }

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("send html mail error: ", e);
            return false;
        }
        return true;
    }

}
```
#### 2.9 开发控制层
```java
import com.oven.service.MailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MailController {

    @Resource
    private MailService mailService;

    @RequestMapping("/send")
    public String send() {
        String to = "779880018@qq.com";
        String title = "测试springboot发送邮件";
        String content = "我是内容";
        boolean result = mailService.send(to, title, content);
        return "send result: " + result;
    }

    @RequestMapping("/sendHtml")
    public String sendHtml() {
        String to = "779880018@qq.com";
        String title = "测试springboot发送邮件";
        String content = "<html><body><h1>我是h1标签</h1><span style='color: red;'>我是内容</span></body></html>";
        boolean result = mailService.sendWithHtml(to, title, content);
        return "send result: " + result;
    }

    @RequestMapping("/sendImg")
    public String sendImg() {
        String to = "779880018@qq.com";
        String title = "测试springboot发送邮件";
        String content = "<html><body>" +
                "<p><h2 style='color: blue'>图片1</h2><img style='width: 120px; height: 120px;' src='cid:img1'></p>" +
                "<p><h2 style='color: blue'>图片2</h2><img style='width: 120px; height: 120px;' src='cid:img2'></p>" +
                "</body></html>";
        String[] cids = new String[]{
                "img1",
                "img2"
        };
        String[] paths = new String[]{
                "/Users/oven/Pictures/C.jpg",
                "/Users/oven/Pictures/avatar.jpg"
        };
        boolean result = mailService.sendWithImageHtml(to, title, content, cids, paths);
        return "send result: " + result;
    }

    @RequestMapping("/sendEnclosure")
    public String sendEnclosure() {
        String to = "779880018@qq.com";
        String title = "测试springboot发送邮件";
        String content = "带附件的邮件";
        String[] paths = new String[]{
                "/Users/oven/Pictures/C.jpg",
                "/Users/oven/Pictures/avatar.jpg"
        };
        boolean result = mailService.sendWithWithEnclosure(to, title, content, paths);
        return "send result: " + result;
    }

}
```
#### 2.10 编辑配置文件
```properties
spring.mail.host=smtp.qq.com
spring.mail.username=503612012@qq.com
spring.mail.password=xxxxxxxxxxxxxxxx
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```
#### 2.11 编译打包运行
### 3. 应用场景