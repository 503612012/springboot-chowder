# springboot炖多租户
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
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
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

@Data
public class User {

    private Integer id;
    private String uname;
    private String pwd;
    private Integer age;

}
```
#### 2.8 开发UserDao类
```java
import com.oven.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends BaseDao {

    public List<User> get() {
        String sql = "select * from t_user";
        return super.getJdbcTemplate().query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("dbid"));
            user.setUname(rs.getString("uname"));
            user.setPwd(rs.getString("pwd"));
            user.setAge(rs.getInt("age"));
            return user;
        });
    }

}
```
#### 2.9 开发UserService类
```java
import com.oven.dao.UserDao;
import com.oven.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    public List<User> get() {
        return userDao.get();
    }

}
```
#### 2.10 开发测试接口类
```java
import com.oven.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private UserService userService;

    @RequestMapping("/get")
    public Object get() {
        return userService.get();
    }

}
```
#### 2.11 编写配置文件
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_test?useUnicode=true&characterEncoding=utf-8
spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
```
#### 2.12 编写组件配置实体类
```java
import lombok.Data;

@Data
public class DataSourceConfig {

    private Long id;
    private String name;
    private String url;
    private String username;
    private String password;
    private String driverClassName;

}
```
#### 2.13 开发租户dao层
```java
import com.oven.config.entity.DataSourceConfig;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class DataSourceConfigDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<DataSourceConfig> getAlll() {
        String sql = "select * from t_datasource_config";
        return this.jdbcTemplate.query(sql, resultMap());
    }

    public DataSourceConfig getByName(String name) {
        String sql = "select * from t_datasource_config where _name = ?";
        List<DataSourceConfig> list = this.jdbcTemplate.query(sql, resultMap(), name);
        return list.size() == 0 ? null : list.get(0);
    }

    private RowMapper<DataSourceConfig> resultMap() {
        return (rs, rowNum) -> {
            DataSourceConfig dataSourceConfig = new DataSourceConfig();
            dataSourceConfig.setId(rs.getLong("id"));
            dataSourceConfig.setName(rs.getString("_name"));
            dataSourceConfig.setPassword(rs.getString("_password"));
            dataSourceConfig.setUsername(rs.getString("username"));
            dataSourceConfig.setUrl(rs.getString("url"));
            dataSourceConfig.setDriverClassName(rs.getString("driverclassname"));
            return dataSourceConfig;
        };
    }

}
```
#### 2.14 开发默认数据源
```java
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DefaultDataSourceConfig {

    @Primary
    @Bean(name = "defaultDataSource")
    @ConfigurationProperties("spring.datasouce")
    public DataSource defaultDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

}
```
#### 2.15 开发租户数据源选择类
```java
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.oven.config.dao.DataSourceConfigDao;
import com.oven.config.entity.DataSourceConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TenantDataSource {

    private final Map<String, DataSource> dataSources = new HashMap<>();

    @Resource
    private DataSourceConfigDao dataSourceConfigDao;

    public Map<String, DataSource> getAll() {
        List<DataSourceConfig> list = dataSourceConfigDao.getAlll();
        Map<String, DataSource> result = new HashMap<>();
        for (DataSourceConfig config : list) {
            DataSource dataSource = getDataSource(config);
            result.put(config.getName(), dataSource);
        }
        return result;
    }

    public DataSource getDataSource(DataSourceConfig config) {
        if (dataSources.containsKey(config.getName())) {
            return dataSources.get(config.getName());
        }
        DataSource dataSource = createDataSource(config);
        dataSources.put(config.getName(), dataSource);
        return dataSource;
    }

    private DataSource createDataSource(DataSourceConfig config) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setUrl(config.getUrl());
        dataSource.setDriverClassName(config.getDriverClassName());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        return dataSource;
    }

}
```
#### 2.16 开发多租户数据源选择适配器
```java
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    @Resource(name = "defaultDataSource")
    private DataSource defaultDataSource;

    @Resource
    private ApplicationContext context;

    static Map<String, DataSource> map = new HashMap<>();

    public static boolean init = false;

    @PostConstruct
    public void load() {
        map.put("defaultDataSource", defaultDataSource);
    }

    @Override
    public DataSource selectAnyDataSource() {
        return map.get("defaultDataSource");
    }

    @Override
    public DataSource selectDataSource(String tenant) {
        if (!init) {
            init = true;
            TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
            map.putAll(tenantDataSource.getAll());
        }
        return map.get(tenant);
    }

}
```
#### 2.18 编写BaseDao类
```java
import com.oven.config.DataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class BaseDao {

    private static final String TENANT_KEY = "tenant";

    @Resource
    private DataSourceBasedMultiTenantConnectionProviderImpl datasourceProvider;

    /**
     * 动态获取jdbcTemplate
     */
    protected JdbcTemplate getJdbcTemplate() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest req = attributes.getRequest();
        String tenant = req.getHeader(TENANT_KEY);
        if (StringUtils.isEmpty(tenant)) {
            return null;
        }
        return new JdbcTemplate(datasourceProvider.selectDataSource(tenant));
    }

}
```
#### 2.18 编译打包运行
### 3. 应用场景