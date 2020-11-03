package com.oven.netty.study.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

import java.io.IOException;

/**
 * MsgPack编码器
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) {
        MessagePack messagePack = new MessagePack();
        // 序列化
        byte[] write = new byte[0];
        try {
            write = messagePack.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.writeBytes(write);
    }

}
