package com.oven.task;

import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {

    /**
     * (fixedRate = 2000) 上一次开始执行时间点之后2秒再执行
     * (fixedDelay = 2000) 上一次执行完毕时间点之后2秒再执行
     * (initialDelay = 1000, fixedRate = 2000) 第一次延迟1秒后执行，之后按fixedRate的规则每2秒执行一次
     * (cron="/10 * * * * ?") cron表达式
     */
    @Scheduled(fixedRate = 2000)
    public void task() {
        System.out.println("do somthing..." + new DateTime().toString("HH:mm:ss"));
    }

}
