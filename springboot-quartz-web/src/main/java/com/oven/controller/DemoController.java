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
    public String startAllJob() {
        try {
            quartzSchedulerConfig.startAllJob();
            return "启动成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "启动失败";
        }
    }

    @RequestMapping("/getInfo")
    public String getJobInfo(String name, String group) {
        String info;
        try {
            info = quartzSchedulerConfig.getJobInfo(name, group);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return null;
        }
        return info;
    }

    @RequestMapping("/modify")
    public String modifyJob(String name, String group, String time) {
        boolean flag;
        try {
            flag = quartzSchedulerConfig.modifyJob(name, group, time);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "修改失败";
        }
        if (flag) {
            return "修改成功";
        }
        return "修改失败";
    }

    @RequestMapping(value = "/pause")
    public String pauseJob(String name, String group) {
        try {
            quartzSchedulerConfig.pauseJob(name, group);
            return "暂停成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "暂停失败";
        }
    }

    @RequestMapping(value = "/pauseAll")
    public String pauseAllJob() {
        try {
            quartzSchedulerConfig.pauseAllJob();
            return "暂停全部成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "暂停全部失败";
        }
    }


    @RequestMapping(value = "/resume")
    public String resume(String name, String group) {
        try {
            quartzSchedulerConfig.resumeJob(name, group);
            return "重启成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "重启失败";
        }
    }

    @RequestMapping(value = "/resumeAll")
    public String resumeAll() {
        try {
            quartzSchedulerConfig.resumeAllJob();
            return "重启全部成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "重启全部失败";
        }
    }

    @RequestMapping(value = "/delete")
    public String delete(String name, String group) {
        try {
            quartzSchedulerConfig.deleteJob(name, group);
            return "删除成功";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "删除失败";
        }
    }

}
