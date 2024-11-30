package com.oven.influxdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>docker run -d -p 2086:8086 --name influxdb influxdb:1.6.3</p>
 *
 * <p>docker exec -it influxdb influx</p>
 *
 * <p>create database test</p>
 */
@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
