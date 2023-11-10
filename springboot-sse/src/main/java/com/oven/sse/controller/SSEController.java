package com.oven.sse.controller;

import com.oven.sse.sse.SseEmitterServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/sse")
public class SSEController {

    /**
     * sse连接
     */
    @GetMapping(value = "/connect/{id}")
    public SseEmitter connect(@PathVariable String id) {
        return SseEmitterServer.connect(id);
    }

    /**
     * sse向指定用户发送消息
     */
    @GetMapping(value = "/send/{id}")
    public Map<String, Object> send(@PathVariable String id, @RequestParam(value = "message", required = false) String message) {
        Map<String, Object> returnMap = new HashMap<>();
        // 向指定用户发送信息
        SseEmitterServer.sendMessage(id, message);
        returnMap.put("message", "向id为" + id + "的用户发送：" + message + "成功!");
        returnMap.put("status", "200");
        returnMap.put("result", null);
        return returnMap;
    }

    /**
     * sse向指定用户发送指定消息
     */
    @GetMapping(value = "/sendCamera/{id}")
    public Map<String, Object> sendCamera(@PathVariable String id, @RequestParam(value = "message", required = false) String message) {
        Map<String, Object> returnMap = new HashMap<>();
        // 向指定用户发送信息
        SseEmitterServer.sendCamera(id, message);
        returnMap.put("message", "向id为" + id + "的用户发送：" + message + "成功!");
        returnMap.put("status", "200");
        returnMap.put("result", null);
        return returnMap;
    }

    /**
     * sse向所有已连接用户发送消息
     */
    @GetMapping(value = "/sendAll")
    public Map<String, Object> sendAll(@RequestParam(value = "message", required = false) String message) {
        Map<String, Object> returnMap = new HashMap<>();
        // 向指定用户发送信息
        SseEmitterServer.sendMessageToAll(message);
        returnMap.put("message", message + "消息发送成功!");
        returnMap.put("status", "200");
        returnMap.put("result", null);
        return returnMap;
    }

    /**
     * sse关闭
     */
    @GetMapping(value = "/close/{id}")
    public Map<String, Object> close(@PathVariable String id) {
        Map<String, Object> returnMap = new HashMap<>();
        // 移除id
        SseEmitterServer.removeClient(id);
        System.out.println("当前连接用户id:" + SseEmitterServer.getIds());
        returnMap.put("message", "连接关闭成功!");
        returnMap.put("status", "200");
        returnMap.put("result", null);
        return returnMap;
    }

}

