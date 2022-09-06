package com.oven;

import com.oven.config.PropertyConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@Slf4j
@SpringBootApplication
public class Application {

    /**
     * 系统启动入口
     */
    public static void main(String[] args) {
        Properties properties = PropertyConfig.loadProperties();
        if (properties == null) {
            log.error("load properties error...");
            return;
        }
        SpringApplication application = new SpringApplication(Application.class);
        application.setDefaultProperties(properties);
        application.run(args);
    }

}