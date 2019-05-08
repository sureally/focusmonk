package com.netease.focusmonk.utils;

import com.netease.focusmonk.common.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author hejiecheng
 * @Date 2019-04-30
 */

@Slf4j
@Component
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, String value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Object getKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    //自动拼接Redis Key
    public static String generateKey(String[] keys) {

        if (keys == null) return null;

        if (keys.length <= 1) return keys[0];

        StringBuilder redisKey = new StringBuilder();
        for (String key : keys) {
            redisKey.append(CommonConstant.REDIS_KEY_SPLICING_SYMBOL).append(key);
        }

        return redisKey.substring(1);
    }
}
