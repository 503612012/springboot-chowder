package com.oven.controller;

import com.oven.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.concurrent.TimeUnit;

@Controller
public class GreetingController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/scene")
    public void greeting(Message message) throws Exception {
        System.out.println("收到：" + message.toString() + "消息");
        for (int i = 0; i < 10; i++) {
            simpMessagingTemplate.convertAndSend("/scene/5217", "Hello, " + HtmlUtils.htmlEscape(message.getContent()) + "!");
            TimeUnit.SECONDS.sleep(2);
        }
    }

}
