# springboot炖swagger2
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
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
        <version>2.8.0</version>
    </dependency>

    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger-ui</artifactId>
        <version>2.8.0</version>
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
#### 2.7 开发用户实体类
```java
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户实体类", description = "用户实体类信息")
public class User {

    @ApiModelProperty(value = "用户主键", dataType = "int", example = "1")
    private Integer id;
    @ApiModelProperty(value = "用户名", dataType = "string", example = "admin")
    private String uname;
    @ApiModelProperty(value = "用户密码", dataType = "string", example = "123456")
    private String pwd;
    @ApiModelProperty(value = "用户年龄", dataType = "int", example = "18")
    private Integer age;

}
```
#### 2.8 开发控制接口类
```java
import com.oven.vo.User;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "用户控制器")
public class DemoController {

    @ApiOperation(value = "添加用户", notes = "添加用户接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 201, message = "系统异常")
    })
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(User user) {
        if ((user.getId() & 1) > 0) {
            return "添加成功";
        } else {
            return "添加失败";
        }
    }

    @ApiOperation(value = "删除用户", notes = "删除用户接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 201, message = "系统异常")
    })
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ApiImplicitParam(name = "id", value = "用户主键", dataType = "int", required = true, example = "1")
    public Object delete(Integer id) {
        if ((id & 1) > 0) {
            return "删除成功";
        } else {
            return "删除失败";
        }
    }

    @ApiOperation(value = "更新用户", notes = "更新用户接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 201, message = "系统异常")
    })
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Object update(User user) {
        if (user.getId() << 1 > 0) {
            return "更新成功";
        } else {
            return "更新失败";
        }
    }

    @ApiOperation(value = "查询用户", notes = "查询用户接口")
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功", response = User.class),
            @ApiResponse(code = 201, message = "系统异常", response = User.class)
    })
    public Object search(User user) {
        return user;
    }

}
```
#### 2.9 开发swagger2配置类
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.oven.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .description("springboot炖swagger2接口文档")
                        .title("springboot炖swagger2接口文档")
                        .contact(new Contact("Oven", "http://www.baidu.com", "xxx@qq.com"))
                        .version("v1.0")
                        .license("Apache2.0")
                        .build());
    }

}
```
#### 2.10 编译打包运行
```http request
http://localhost:8080/swagger-ui.html
```
### 3. 应用场景