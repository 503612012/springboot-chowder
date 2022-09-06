package com.oven.netty.study.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MsgPack编码器
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object> {

    /**
     * MsgPackEncoder 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MsgPackEncoder.class);

    /**
     * 编码 Object -> byte[]
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
        LOGGER.info("\n\t⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" +
                "\t├ [编码]: {}\n" +
                "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓", msg);
        MessagePack messagePack = new MessagePack();
        // 序列化
        byte[] write = messagePack.write(msg);
        out.writeBytes(write);
    }

}
