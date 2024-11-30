package com.oven.service;

import com.lmax.disruptor.RingBuffer;
import com.oven.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class TaskMessageService {

    @Resource(name = "taskMessageQueue")
    private RingBuffer<Message> taskMessageQueue;

    public void sendMessage(String msg) {
        log.info("开始发送消息：{}", msg);
        long sequence = taskMessageQueue.next();
        try {
            Message message = taskMessageQueue.get(sequence);
            message.setMessage(msg);
            log.info("往队列中添加消息：sequence={}，message={}", sequence, message);
        } catch (Exception e) {
            log.error("发送消息异常：", e);
        } finally {
            // 发布Event，激活观察者去消费，将sequence传递给改消费者
            // 注意最后的publish方法必须放在finally中以确保必须得到调用
            // 如果某个请求的sequence未被提交将会堵塞后续的发布操作或者其他的producer
            taskMessageQueue.publish(sequence);
        }
    }

}
