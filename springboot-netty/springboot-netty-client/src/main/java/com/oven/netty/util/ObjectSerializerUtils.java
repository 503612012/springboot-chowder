package com.oven.netty.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 对象序列化工具
 */
public class ObjectSerializerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectSerializerUtils.class);

    /**
     * 反序列化
     */
    public static Object deSerilizer(byte[] data) {
        if (data != null && data.length > 0) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bis);
                return ois.readObject();
            } catch (Exception e) {
                LOGGER.info("[异常信息] {}", e.getMessage());
                e.printStackTrace();
            }
            return null;
        } else {
            LOGGER.info("[反序列化] 入参为空");
            return null;
        }
    }

    /**
     * 序列化对象
     */
    public static byte[] serilizer(Object obj) {
        if (obj != null) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(obj);
                oos.flush();
                oos.close();
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

}
