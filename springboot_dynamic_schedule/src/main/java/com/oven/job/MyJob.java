package com.oven.job;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class MyJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        // 获取参数
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        String param1 = (String) dataMap.get("param1");
        String param2 = (String) dataMap.get("param2");
        String param3 = (String) dataMap.get("param3");

        System.out.println(param1);
        System.out.println(param2);
        System.out.println(param3);
    }

}
