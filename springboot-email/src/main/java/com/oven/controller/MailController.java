package com.oven.controller;

import com.oven.service.MailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MailController {

    @Resource
    private MailService mailService;

    @RequestMapping("/send")
    public String send() {
        String to = "1234567@qq.com";
        String title = "测试springboot发送邮件";
        String content = "我是内容";
        boolean result = mailService.send(to, title, content);
        return "send result: " + result;
    }

    @RequestMapping("/sendHtml")
    public String sendHtml() {
        String to = "1234567@qq.com";
        String title = "测试springboot发送邮件";
        String content = "<html><body><h1>我是h1标签</h1><span style='color: red;'>我是内容</span></body></html>";
        boolean result = mailService.sendWithHtml(to, title, content);
        return "send result: " + result;
    }

    @RequestMapping("/sendImg")
    public String sendImg() {
        String to = "1234567@qq.com";
        String title = "测试springboot发送邮件";
        String content = "<html><body>" +
                "<p><h2 style='color: blue'>图片1</h2><img style='width: 120px; height: 120px;' src='cid:img1'></p>" +
                "<p><h2 style='color: blue'>图片2</h2><img style='width: 120px; height: 120px;' src='cid:img2'></p>" +
                "</body></html>";
        String[] cids = new String[]{
                "img1",
                "img2"
        };
        String[] paths = new String[]{
                "/Users/oven/Pictures/C.jpg",
                "/Users/oven/Pictures/avatar.jpg"
        };
        boolean result = mailService.sendWithImageHtml(to, title, content, cids, paths);
        return "send result: " + result;
    }

    @RequestMapping("/sendEnclosure")
    public String sendEnclosure() {
        String to = "1234567@qq.com";
        String title = "测试springboot发送邮件";
        String content = "带附件的邮件";
        String[] paths = new String[]{
                "/Users/oven/Pictures/C.jpg",
                "/Users/oven/Pictures/avatar.jpg"
        };
        boolean result = mailService.sendWithWithEnclosure(to, title, content, paths);
        return "send result: " + result;
    }

}
