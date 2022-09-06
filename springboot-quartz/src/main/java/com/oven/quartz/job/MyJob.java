package com.oven.quartz.job;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class MyJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        System.out.println("任务开始执行。。。" + new DateTime().toString("HH:mm:ss"));
    }

}
