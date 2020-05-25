# springboot炖restdoc
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
#### 2.4 pom.xml文件中加入springboot-starter依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.restdocs</groupId>
        <artifactId>spring-restdocs-mockmvc</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```
#### 2.5 pom.xml文件中加入asciidoctor打包插件
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.asciidoctor</groupId>
            <artifactId>asciidoctor-maven-plugin</artifactId>
            <version>1.5.3</version>
            <executions>
                <execution>
                    <id>generate-docs</id>
                    <phase>prepare-package</phase>
                    <goals>
                        <goal>process-asciidoc</goal>
                    </goals>
                    <configuration>
                        <sourceDocumentName>index.adoc</sourceDocumentName>
                        <backend>html</backend>
                        <attributes>
                            <snippets>${project.build.directory}/snippets</snippets>
                        </attributes>
                    </configuration>
                </execution>
            </executions>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.restdocs</groupId>
                    <artifactId>spring-restdocs-asciidoctor</artifactId>
                    <version>1.2.1.RELEASE</version>
                </dependency>
            </dependencies>
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
    private String uname;
    private String pwd;
    private Integer age;

}
```
#### 2.8 开发统一返回实体类
```java
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultInfo<T> {

    private int code;
    private String message;
    private T data;

}
```
#### 2.9 开发控制接口类
```java
import com.oven.vo.ResultInfo;
import com.oven.vo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class DemoController {

    @GetMapping(value = "/test")
    public Map<String, String> test() {
        return Collections.singletonMap("message", "springboot_restdoc");
    }

    @GetMapping(value = "/getById")
    public ResultInfo<User> getById(Integer id) {
        User user = new User(id, "admin", "123456", 18);
        return new ResultInfo<>(200, "请求成功", user);
    }

    @PostMapping(value = "/getAll")
    public ResultInfo<List<User>> getAll(User user) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new User(i + 1, user.getUname() + i, "123456", user.getAge()));
        }
        return new ResultInfo<>(200, "请求成功", list);
    }

    @GetMapping(value = "/delete/{id}")
    public ResultInfo<Object> delete(@PathVariable("id") int id) {
        return new ResultInfo<>(200, "删除成功", "删除用户【" + id + "】成功");
    }

}
```
#### 2.10 开发测试类
```java
import com.oven.controller.DemoController;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DemoController.class)
public class DemoTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/snippets");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void test() throws Exception {
        this.mockMvc.perform(get("/test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("test", responseFields(
                        fieldWithPath("message").description("测试接口")
                )));
    }

    @Test
    public void getById() throws Exception {
        this.mockMvc.perform(get("/getById").param("id", "1"))
                .andExpect(status().isOk())
                .andDo(document("getById", requestParameters(
                        parameterWithName("id").description("用户主键")
                )));
    }

    @Test
    public void getAll() throws Exception {
        this.mockMvc.perform(post("/getAll")
                .param("uname", "admin")
                .param("age", "18"))
                .andExpect(status().isOk())
                .andDo(document("getAll", requestParameters(
                        parameterWithName("uname").description("用户名"),
                        parameterWithName("age").description("年龄"))));
    }

    @Test
    public void delete() throws Exception {
        this.mockMvc.perform(get("/delete/{id}", 1))
                .andExpect(status().isOk())
                .andDo(document("delete", pathParameters(
                        parameterWithName("id").description("用户主键")
                )));
    }

}
```
#### 2.11 在main目录下创建asciidoc目录，编写文件index.adoc
```scaladoc
____
springboot_restdoc
____


=======
____
测试例子
____
'''
include::{snippets}/test/http-request.adoc[]
include::{snippets}/test/http-response.adoc[]
=======


=======
____
通过用户主键查询用户
____
'''
include::{snippets}/getById/http-request.adoc[]
include::{snippets}/getById/http-response.adoc[]
include::{snippets}/getById/request-parameters.adoc[]
=======


=======
____
获取全部用户
____
'''
include::{snippets}/getAll/http-request.adoc[]
include::{snippets}/getAll/http-response.adoc[]
include::{snippets}/getAll/request-parameters.adoc[]
=======


=======
____
删除用户
____
'''
include::{snippets}/delete/http-request.adoc[]
include::{snippets}/delete/http-response.adoc[]
include::{snippets}/delete/path-parameters.adoc[]
=======
```
#### 2.12 编译打包运行
### 3. 应用场景