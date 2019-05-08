package com.netease.focusmonk.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName RedisServiceImpl
 * @Author konghaifeng
 * @Date 2019/5/8 14:15
 **/

@Service
public class RedisServiceImpl {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Map<String, Object> valueMap) {
        redisTemplate.opsForHash().putAll(key, valueMap);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> T get(String key, Class<T> clazz) {

        Object result = redisTemplate.opsForValue().get(key);

        if (clazz.isInstance(result)) {
            return clazz.cast(result);
        }

        return null;
    }

    public Boolean containKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
