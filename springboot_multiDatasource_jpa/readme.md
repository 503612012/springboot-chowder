# springboot炖multiDatasource_jpa
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
#### 2.4 pom.xml文件中加入相关依赖
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.1.10</version>
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
#### 2.7 开发User类
```java
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @Column(name = "dbid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "uname")
    private String uname;
    @Column(name = "pwd")
    private String pwd;
    @Column(name = "age")
    private Integer age;

}
```
#### 2.8 编写配置文件
```yaml
spring:
  datasource:
    druid:
      mysql:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/db_test?useUnicode=true&characterEncoding=utf-8
        username: root
        password: root
      oracle:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/db_test2?useUnicode=true&characterEncoding=utf-8
        username: root
        password: root
  jpa:
    show-sql: true
```
#### 2.9 开发数据源配置文件
```java
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid.mysql")
    public DataSource mysqlDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.oracle")
    public DataSource oracleDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

}
```
#### 2.10 开发mysql-jpa适配数据源配置
```java
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.oven.dao.mysql", entityManagerFactoryRef = "mysqlEntityManagerFactoryRef", transactionManagerRef = "mysqlTransactionManagerRef")
public class JpaConfigMySql {

    @Resource
    @Qualifier(value = "mysqlDataSource")
    private DataSource mysqlDataSource;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactoryRef(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(mysqlDataSource)
                .packages("com.oven.vo")
                .build();
    }

    @Bean
    @SuppressWarnings("ConstantConditions")
    public PlatformTransactionManager mysqlTransactionManagerRef(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactoryBean = mysqlEntityManagerFactoryRef(builder);
        return new JpaTransactionManager(mysqlEntityManagerFactoryBean.getObject());
    }

}
```
#### 2.11 开发oracle-jpa适配数据源配置
```java
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.oven.dao.oracle", entityManagerFactoryRef = "oracleEntityManagerFactoryRef", transactionManagerRef = "oracleTransactionManagerRef")
public class JpaConfigOracle {

    @Resource
    @Qualifier(value = "oracleDataSource")
    private DataSource oracleDataSource;

    @Resource
    private JpaProperties jpaProperties;

    @Bean
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactoryRef(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(oracleDataSource)
                .packages("com.oven.vo")
                .build();
    }

    @Bean
    @SuppressWarnings("ConstantConditions")
    public PlatformTransactionManager oracleTransactionManagerRef(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean oracleEntityManagerFactoryBean = oracleEntityManagerFactoryRef(builder);
        return new JpaTransactionManager(oracleEntityManagerFactoryBean.getObject());
    }

}
```
#### 2.12 开发UserDao类-mysql
```java
import com.oven.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MysqlUserDao extends JpaRepository<User, Integer> {
}
```
#### 2.13 开发UserDao类-oracle
```java
import com.oven.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OracleUserDao extends JpaRepository<User, Integer> {
}
```
#### 2.14 开发UserService类
```java
import com.oven.dao.mysql.MysqlUserDao;
import com.oven.dao.oracle.OracleUserDao;
import com.oven.vo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private MysqlUserDao mysqlUserDao;
    @Resource
    private OracleUserDao oracleUserDao;

    public List<User> getMysql() {
        return mysqlUserDao.findAll();
    }

    public List<User> getOracle() {
        return oracleUserDao.findAll();
    }

}
```
#### 2.15 开发测试接口类
```java
import com.oven.service.UserService;
import com.oven.vo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DemoController {
    
    @Resource
    private UserService userService;

    @RequestMapping("/getMysql")
    public List<User> getMysql() {
        return userService.getMysql();
    }

    @RequestMapping("/getOracle")
    public List<User> getOracle() {
        return userService.getOracle();
    }
    
}
```
#### 2.16 编译打包运行
### 3. 应用场景