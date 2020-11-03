package com.oven.netty.study;

import com.oven.netty.study.echo.megpack.MessagePackClient;
import com.oven.netty.study.websocket.WebSocketClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudyClientApplicationTests {

    /**
     * 测试端口
     */
    private final int port = 8080;

    /**
     * 测试IP
     */
    private final String host = "127.0.0.1";

    /**
     * 启动未解决粘包/拆包问题的netty客户端
     */
    @Test
    public void startNettyClient1() throws Exception {
        new com.oven.netty.study.time.demo1.TimeClient().connect(port, host);
    }

    /**
     * 启动模拟粘包/拆包问题的netty客户端
     */
    @Test
    public void startNettyClient2() throws Exception {
        new com.oven.netty.study.time.demo2.TimeClient().connect(port, host);
    }

    /**
     * 启动已解决粘包/拆包问题的netty客户端 - LineBasedFrameDecoder 实现
     */
    @Test
    public void startNettyClient3() throws Exception {
        new com.oven.netty.study.time.demo3.TimeClient().connect(port, host);
    }

    /**
     * 启动已解决粘包/拆包问题的netty客户端 - DelimiterBasedFrameDecoder 实现
     */
    @Test
    public void startNettyClient4() throws Exception {
        new com.oven.netty.study.echo.delimiter.EchoClient().connect(port, host);
    }

    /**
     * 启动已解决粘包/拆包问题的netty服务 - FixedLengthFrameDecoder 实现
     */
    @Test
    public void startNettyServer5() throws Exception {
        new com.oven.netty.study.echo.fixlength.EchoClient().connect(port, host);
    }

    /**
     * 启动不考虑粘包/拆包问题 基于MessagePack编解码的Netty客户端
     */
    @Test
    public void testMessagePackEchoClient() throws Exception {
        new MessagePackClient().connect(port, host, 20);
    }

    /**
     * 启动WebSocketClient
     */
    @Test
    public void startWebSocketClient() throws Exception {
        new WebSocketClient().connect(9999, host, "Oven");
    }

    @Test
    public void startWebSocketClient2() throws Exception {
        new WebSocketClient().connect(9999, host, "Oven");
    }

}