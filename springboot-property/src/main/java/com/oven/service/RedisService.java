package com.oven.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 查询
     *
     * @param key 键 不可为空
     */
    public <T> T get(String key) {
        T obj = null;
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            //noinspection unchecked
            obj = (T) operations.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 设置
     *
     * @param key 键 不可为空
     * @param obj 值 不可为空
     */
    public <T> void set(String key, T obj) {
        set(key, obj, null);
    }

    /**
     * 设置键值，这会直接覆盖掉给定键之前映射的值
     *
     * @param key        键 不可为空
     * @param obj        值 不可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     */
    public <T> void set(String key, T obj, Long expireTime) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        if (obj == null) {
            return;
        }
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, obj);
        if (null != expireTime) {
            redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 移除
     *
     * @param key 键 不可为空
     */
    public void remove(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        redisTemplate.delete(key);
    }

    /**
     * 是否存在
     *
     * @param key 键 不可为空
     */
    public boolean contains(String key) {
        boolean exists = false;
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        Object obj = get(key);
        if (obj != null) {
            exists = true;
        }
        return exists;
    }

}
