package com.oven.quartz.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.oven.quartz.job.MyJob;
import org.quartz.*;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(MyJob.class).storeDurably().build();
    }

    @Bean
    public Trigger trigger() {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(2)
                .repeatForever();
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withSchedule(builder)
                .build();
    }

    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.quartz.properties.org.quartz.datasource")
    public DataSource quartzDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

}
