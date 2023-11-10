package com.oven.sse.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
public class SseEmitterServer {

    /**
     * 当前连接数
     */
    private static final AtomicInteger count = new AtomicInteger(0);

    private static final Map<String, SseEmitter> SSE_EMITTER_MAP = new ConcurrentHashMap<>();

    public static SseEmitter connect(String clientId) {
        // 设置超时时间，0表示不过期，默认是30秒，超过时间未完成会抛出异常
        SseEmitter sseemitter = new SseEmitter(0L);
        // 长链接完成后回调接口（即关闭连接时调用）
        sseemitter.onCompletion(completionCallBack(clientId));
        // 连接超时回调
        sseemitter.onTimeout(timeoutCallBack(clientId));
        // 异常回调
        sseemitter.onError(errorCallBack(clientId));
        SSE_EMITTER_MAP.put(clientId, sseemitter);
        // 数量+1
        count.getAndIncrement();
        log.info("创建sse连接，连接客户端ID：{}，当前连接数量：{}", clientId, count.intValue());
        return sseemitter;
    }

    /**
     * 给指定客户端发消息
     */
    public static void sendMessage(String clientId, String message) {
        if (SSE_EMITTER_MAP.containsKey(clientId)) {
            try {
                SSE_EMITTER_MAP.get(clientId).send(message);
            } catch (IOException e) {
                log.error("sse推送消息异常，客户端ID：{}", clientId);
                removeClient(clientId);
            }
        }
    }

    /**
     * 发送指定消息
     */
    public static void sendCamera(String clientId, String message) {
        if (SSE_EMITTER_MAP.containsKey(clientId)) {
            try {
                SSE_EMITTER_MAP.get(clientId).send(SseEmitter.event().data(message).name("camera"));
            } catch (IOException e) {
                log.error("sse推送消息异常，客户端ID：{}", clientId);
                removeClient(clientId);
            }
        }
    }

    /**
     * 广播
     */
    public static void sendMessageToAll(String message) {
        SSE_EMITTER_MAP.forEach((k, v) -> {
            try {
                v.send(message, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                log.error("sse广播消息异常，客户端ID：{}", k);
                removeClient(k);
            }
        });
    }

    /**
     * 群发消息
     */
    public static void sendMessageToAll(String message, Set<String> clientIds) {
        clientIds.forEach(clientId -> sendMessage(clientId, message));
    }

    /**
     * 移除客户端
     */
    public static void removeClient(String clientId) {
        SSE_EMITTER_MAP.remove(clientId);
        // 数量-1
        count.getAndDecrement();
        log.info("断开客户端：{}", clientId);
    }

    public static List<String> getIds() {
        return new ArrayList<>(SSE_EMITTER_MAP.keySet());
    }

    public static int getClientCount() {
        return count.intValue();
    }

    private static Runnable completionCallBack(String clientId) {
        return () -> {
            SSE_EMITTER_MAP.remove(clientId);
            // 数量-1
            count.getAndDecrement();
            log.info("结束连接：{}，当前连接数量：{}", clientId, count.intValue());
        };
    }

    private static Runnable timeoutCallBack(String clientId) {
        return () -> {
            log.info("连接超时：{}", clientId);
            removeClient(clientId);
        };
    }

    private static Consumer<Throwable> errorCallBack(String clientId) {
        return throwable -> {
            log.error("连接异常：{}", clientId);
            removeClient(clientId);
        };
    }

}

