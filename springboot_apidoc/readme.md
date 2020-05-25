# springboot炖apidoc
### 1. 先睹为快
### 2. 安装apidoc
```shell script
npm install apidoc -g
```
### 3. 实现原理
#### 3.1 新建项目
#### 3.2 创建maven目录结构，以及pom.xml文件
#### 3.3 pom.xml文件中加入spring-boot-starter-parent
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
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
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
#### 3.7 开发用户实体类
```java
import lombok.Data;

@Data
public class User {

    private Integer id;
    private String uname;
    private String pwd;
    private Integer age;

}
```
#### 3.8 开发控制接口类
```java
import com.oven.vo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    /**
     * @api {POST} /add 添加用户
     * @apiGroup users
     * @apiVersion v1.0.0
     * @apiDescription 添加用户接口
     * @apiParam {String} uname 用户名
     * @apiParam {String} pwd 用户密码
     * @apiParam {Integer} age 年龄
     * @apiParamExample {json} 请求样例：
     * ?uname=admin&pwd=123&age=18
     * @apiSuccess (200) {String} data 响应信息
     * @apiSuccess (200) {int} code 请求状态码
     * @apiSuccess (201) {String} data 错误信息
     * @apiSuccess (201) {int} code 请求状态码
     * @apiSuccessExample {json} 返回样例:
     * {code: 200, data: '添加成功'}
     * @apiSuccessExample {json} 返回样例:
     * {code: 201, data: '添加失败'}
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Object add(User user) {
        System.out.println(user);
        if ((user.getId() & 1) > 0) {
            return "添加成功";
        } else {
            return "添加失败";
        }
    }

    /**
     * @api {get} /search/ 查询用户息
     * @apiGroup users
     * @apiVersion v1.0.0
     * @apiDescription 查询用户息接口
     * @apiParam {String} uname 用户名
     * @apiParam {String} pwd 密码
     * @apiParam {Integer} age 年龄
     * @apiParamExample {json} 请求样例：
     * ?uname=admin&pwd=123&age=18
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 请求成功
     * {
     *      "code": "200",
     *      "data": [{
     *                   "id": "1",
     *                   "uname": "admin"
     *                   "pwd": "123"
     *                   "age": 18
     *              }]
     * }
     * @apiErrorExample Error-Response:
     * HTTP/1.1 201 接口异常
     * {
     *      "code": "201",
     *      "data": "系统异常"
     * }
     */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public Object search(User user) {
        return user;
    }

}
```
#### 3.9 项目根目录增加apidoc.json文件
```json
{
    "name": "springboot_apidoc",
    "version": "v1.0.0",
    "description": "springboot_apidoc的接口文档"
}
```
#### 3.10 生成apidoc文档
```shell script
apidoc -i springboot_apidoc/ -o springboot_apidoc/apidoc
```
### 4. 应用场景