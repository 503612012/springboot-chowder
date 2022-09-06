package com.oven.service;

import com.oven.config.QuartzSchedulerConfig;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TaskService {

    @Resource
    private QuartzSchedulerConfig quartzSchedulerConfig;

    /**
     * 创建任务
     */
    @SuppressWarnings("unchecked")
    public void create(String name, String group) throws Exception {
        Class<? extends QuartzJobBean> clazz = (Class<? extends QuartzJobBean>) Class.forName("com.oven.job.MyJob");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("param1", "param1");
        jobDataMap.put("param2", "param2");
        jobDataMap.put("param3", "param3");
        String cron = "0/5 * * * * ?";
        quartzSchedulerConfig.create(clazz, name, group, cron, jobDataMap);
    }

    /**
     * 删除任务
     */
    public void delete(String name, String group) throws SchedulerException {
        quartzSchedulerConfig.delete(name, group);
    }

    /**
     * 暂停任务
     */
    public void pause(String name, String group) throws SchedulerException {
        quartzSchedulerConfig.pause(name, group);
    }

    /**
     * 恢复任务
     */
    public void resume(String name, String group) throws SchedulerException {
        quartzSchedulerConfig.resume(name, group);
    }

}
