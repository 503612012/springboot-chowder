# springboot炖esper
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
        <groupId>com.espertech</groupId>
        <artifactId>esper</artifactId>
        <version>6.1.0</version>
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
#### 2.7 开发esper配置类
```java
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EsperConfig {

    public final static Map<String, EPStatement> STATEMENT_WRAP = new HashMap<>();

    @Bean
    public EPServiceProvider epServiceProvider() {
        com.espertech.esper.client.Configuration config = new com.espertech.esper.client.Configuration();

        Map<String, Object> mobileLocation = new HashMap<>();
        mobileLocation.put("location", String.class);
        mobileLocation.put("phone", Integer.class);

        config.addEventType("mobileLocation", mobileLocation);

        return EPServiceProviderManager.getDefaultProvider(config);
    }

    @Bean
    public EPAdministrator epAdministrator() {
        return epServiceProvider().getEPAdministrator();
    }

}
```
#### 2.8 开发数据1变更监听
```java
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class LocationListener1 implements UpdateListener {
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        if (newEvents != null) {
            System.out.println(String.format(" 111 匹配成功，批到的位置为：%s，要发送的手机号为：%s，原始信息内容：%s", newEvents[0].get("location"), newEvents[0].get("phone"), newEvents[0].getUnderlying()));
        }
    }
}
```
#### 2.9 开发数据2变更监听
```java
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class LocationListener2 implements UpdateListener {
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        if (newEvents != null) {
            System.out.println(String.format(" 222 匹配成功，批到的位置为：%s，要发送的手机号为：%s，原始信息内容：%s", newEvents[0].get("location"), newEvents[0].get("phone"), newEvents[0].getUnderlying()));
        }
    }
}
```
#### 2.10 开发控制层
```java
import com.espertech.esper.client.*;
import com.oven.config.EsperConfig;
import com.oven.listener.LocationListener1;
import com.oven.listener.LocationListener2;
import com.oven.util.EsperUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EplController {

    @Resource(name = "epServiceProvider")
    private EPServiceProvider provider;
    @Resource(name = "epAdministrator")
    private EPAdministrator epAdministrator;

    @RequestMapping("/start1")
    public String start1() {
        if (null != EsperConfig.STATEMENT_WRAP.get("1")) {
            return "esper1已经启动！";
        }

        String epl = "select location, phone from mobileLocation where location in ('2', '1', '3')";
        EPStatement statement = epAdministrator.createEPL(epl);
        statement.addListener(new LocationListener1());
        statement.start();
        EsperConfig.STATEMENT_WRAP.put("1", statement);
        System.out.println(" 111 启动成功！");
        return " 111 启动成功！";
    }

    @RequestMapping("/start2")
    public String start2() {
        if (null != EsperConfig.STATEMENT_WRAP.get("2")) {
            return "esper2已经启动！";
        }

        String epl = "select location, phone from mobileLocation where location in ('4', '3', '5')";
        EPStatement statement = epAdministrator.createEPL(epl);
        statement.addListener(new LocationListener2());
        statement.start();
        EsperConfig.STATEMENT_WRAP.put("2", statement);
        System.out.println(" 222 启动成功！");
        return " 222 启动成功！";
    }

    @RequestMapping("/stop1")
    public String stop1() {
        EPStatement statement = EsperConfig.STATEMENT_WRAP.get("1");
        if (null == statement) {
            return "esper1 not exist!";
        }

        statement.stop();
        EsperConfig.STATEMENT_WRAP.remove("1");
        return " 111 停止成功！";
    }

    @RequestMapping("/stop2")
    public String stop2() {
        EPStatement statement = EsperConfig.STATEMENT_WRAP.get("2");
        if (null == statement) {
            return "esper2 not exist!";
        }

        statement.stop();
        EsperConfig.STATEMENT_WRAP.remove("2");
        return " 222 停止成功！";
    }

    @RequestMapping("/start3")
    public String start3() {
        if (null != EsperConfig.STATEMENT_WRAP.get("3")) {
            return "esper3已经启动！";
        }

        String epl = "select ip,url,count(*) as num from accesslog.win:time_batch(5 sec) group by ip,url order by num desc";
        EPStatement statements = EsperUtil.getAdministrator().createEPL(epl);
        statements.addListener((newData, oldData, statement, runtime) -> {
            for (EventBean item : newData) {
                String ip = (String) item.get("ip");
                String url = (String) item.get("url");
                long num = (long) item.get("num");
                System.out.printf("5秒钟内用户%s访问接口%s累计%d次\n", ip, url, num);
            }
        });
        statements.start();
        EsperConfig.STATEMENT_WRAP.put("3", statements);
        System.out.println(" 333 启动成功！");
        return " 333 启动成功！";
    }

    @RequestMapping("/send")
    public String send(String location, String phone, String ip, String url) {
        EPRuntime runtime = provider.getEPRuntime();
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(url)) {
            map.put("ip", ip);
            map.put("url", url);
            EsperUtil.getProvider().getEPRuntime().sendEvent(map, "accesslog");
            System.out.println("发送 333 事件成功！");
            return "发送成功！";
        }
        map.put("location", location);
        map.put("phone", phone);

        runtime.sendEvent(map, "mobileLocation");
        return "发送成功！";
    }

}
```
#### 2.11 开发工具类
```java
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

import java.util.HashMap;
import java.util.Map;

public class EsperUtil {

    private static final EPAdministrator administrator;
    private static final EPServiceProvider provider;

    static {
        provider = EPServiceProviderManager.getDefaultProvider();
        administrator = provider.getEPAdministrator();

        Map<String, Object> accesslog = new HashMap<>();
        accesslog.put("ip", String.class);
        accesslog.put("url", Integer.class);

        // 注册accesslog到esper
        administrator.getConfiguration().addEventType("accesslog", accesslog);
    }

    public static EPAdministrator getAdministrator() {
        return administrator;
    }

    public static EPServiceProvider getProvider() {
        return provider;
    }

}
```
#### 2.12 编译打包运行
### 3. 应用场景