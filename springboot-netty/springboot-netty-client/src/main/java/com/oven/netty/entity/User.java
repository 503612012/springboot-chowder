package com.oven.netty.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * 用户实体
 */
@Data
@Message
@NoArgsConstructor
public class User implements Serializable {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = -5462474276911290451L;

    /**
     * 编号
     */
    private int id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 分数
     */
    private double source;

    /**
     * 领导
     */
    private User leader;

    public byte[] codeC() {
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        byte[] bytes = this.name.getBytes();
        allocate.putInt(bytes.length);
        allocate.put(bytes);
        allocate.putInt(this.id);
        allocate.flip();
        byte[] res = new byte[allocate.remaining()];
        allocate.get(res);
        return res;
    }

    public byte[] codeC(ByteBuffer buffer) {
        buffer.clear();
        byte[] bytes = this.name.getBytes();
        buffer.putInt(bytes.length);
        buffer.put(bytes);
        buffer.putInt(this.id);
        buffer.flip();
        byte[] res = new byte[buffer.remaining()];
        buffer.get(res);
        return res;
    }

}
