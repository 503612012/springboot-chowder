package com.oven.task;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Component
public class DynamicScheduleTask implements SchedulingConfigurer {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private String getCron(String key) {
        String sql = "select cron from t_crontab where _key = ?";
        return jdbcTemplate.queryForObject(sql, String.class, key);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(this::doSomething, triggerContext -> {
            String cron = this.getCron("template_key");
            if (StringUtils.isEmpty(cron)) {
                System.out.println("cron is null...");
            }
            return new CronTrigger(cron).nextExecutionTime(triggerContext);
        });
    }

    private void doSomething() {
        System.out.println("do something..." + new DateTime().toString("HH:mm:ss"));
    }

}
