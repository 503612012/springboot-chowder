package com.oven.handler;

import com.lmax.disruptor.EventHandler;
import com.oven.entity.Message;
import lombok.extern.slf4j.Slf4j;

/**
 * 任务消息处理器
 */
@Slf4j
public class TaskEventHandler implements EventHandler<Message> {

    @Override
    public void onEvent(Message message, long sequence, boolean endOfBatch) {
        try {
            log.info("任务消息处理器收到消息sequence={}，endOfBatch={}，消息内容={}", message, sequence, endOfBatch);
        } catch (Exception e) {
            log.error("任务消息处理器异常：", e);
        }
    }

}
