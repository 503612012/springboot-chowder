package com.oven.config;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.oven.entity.Message;
import com.oven.entity.MessageEventFactory;
import com.oven.handler.TaskEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class DisruptorManager {

    @Bean("taskMessageQueue")
    public RingBuffer<Message> taskMessageQueue() {
        // 定义用于事件处理的线程池， Disruptor通过java.util.concurrent.ExecutorService提供的线程来触发consumer的事件处理
        ExecutorService executor = Executors.newFixedThreadPool(8);

        // 指定事件工厂
        MessageEventFactory factory = new MessageEventFactory();

        // 指定ringBuffer字节大小，必须为2的N次方（能将求模运算转为位运算提高效率），否则将影响效率
        int bufferSize = 1024 * 256;

        // 单线程模式，获取额外的性能
        Disruptor<Message> disruptor = new Disruptor<>(factory, bufferSize, executor, ProducerType.SINGLE, new BlockingWaitStrategy());

        // 设置事件业务处理器---消费者
        disruptor.handleEventsWith(new TaskEventHandler());

        // 启动disruptor线程
        disruptor.start();

        // 获取指定ringBuffer字节大小r环，用于接取生产者生产的事件
        return disruptor.getRingBuffer();
    }

}
