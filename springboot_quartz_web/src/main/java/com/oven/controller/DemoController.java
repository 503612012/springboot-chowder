package com.oven.controller;

import com.oven.quartz.config.QuartzSchedulerConfig;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/quartz")
public class DemoController {

    @Resource
    private QuartzSchedulerConfig quartzSchedulerConfig;

    @RequestMapping("/startAll")
    public void startAllJob() {
        try {
            quartzSchedulerConfig.startAllJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getInfo")
    public String getJobInfo(String name, String group) {
        String info = null;
        try {
            info = quartzSchedulerConfig.getJobInfo(name, group);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return info;
    }

    @RequestMapping("/modify")
    public boolean modifyJob(String name, String group, String time) {
        boolean flag = true;
        try {
            flag = quartzSchedulerConfig.modifyJob(name, group, time);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @RequestMapping(value = "/pause")
    public void pauseJob(String name, String group) {
        try {
            quartzSchedulerConfig.pauseJob(name, group);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/pauseAll")
    public void pauseAllJob() {
        try {
            quartzSchedulerConfig.pauseAllJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "/resume")
    public void resume(String name, String group) {
        try {
            quartzSchedulerConfig.resumeJob(name, group);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/resumeAll")
    public void resumeAll() {
        try {
            quartzSchedulerConfig.resumeAllJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/delete")
    public void delete(String name, String group) {
        try {
            quartzSchedulerConfig.deleteJob(name, group);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
