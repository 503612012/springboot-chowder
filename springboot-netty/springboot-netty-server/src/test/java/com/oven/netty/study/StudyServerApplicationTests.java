package com.oven.netty.study;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.oven.netty.study.echo.megpack.MessagePackServer;
import com.oven.netty.study.websocket.WebSocketServer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudyServerApplicationTests {

    private final int port = 8080;

    /**
     * 启动不考虑粘包/拆包问题的netty服务
     */
    @Test
    public void startNettyServer1() throws Exception {
        new com.oven.netty.study.time.demo1.TimeServer().bind(port);
    }

    /**
     * 启动模拟粘包/拆包问题的netty服务
     */
    @Test
    public void startNettyServer2() throws Exception {
        new com.oven.netty.study.time.demo2.TimeServer().bind(port);
    }

    /**
     * 启动已解决粘包/拆包问题的netty服务 - LineBasedFrameDecoder 实现
     */
    @Test
    public void startNettyServer3() throws Exception {
        new com.oven.netty.study.time.demo3.TimeServer().bind(port);
    }

    /**
     * 启动已解决粘包/拆包问题的netty服务 - DelimiterBasedFrameDecoder 实现
     */
    @Test
    public void startNettyServer4() throws Exception {
        new com.oven.netty.study.echo.delimiter.EchoServer().bind(port);
    }

    /**
     * 启动已解决粘包/拆包问题的netty服务 - FixedLengthFrameDecoder 实现
     */
    @Test
    public void startNettyServer5() throws Exception {
        new com.oven.netty.study.echo.fixlength.EchoServer().bind(port);
    }

    /**
     * 启动不考虑粘包/拆包问题 基于MessagePack编解码的Netty服务
     */
    @Test
    public void testMessagePackEchoServer() throws Exception {
        new MessagePackServer().bind(port);
    }

    /**
     * 启动WebSocket服务器
     */
    @Test
    public void startWebSocketServer() throws Exception {
        new WebSocketServer().run(9999);
    }

}