package com.netease.focusmonk.service;

import com.netease.focusmonk.utils.RedisUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisServiceImpl
 * @Author konghaifeng
 * @Date 2019/5/8 14:15
 **/

@Service
public class RedisServiceImpl {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void setWithTTL(String key, String value, long ttl) {
        stringRedisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
    }

    public void setObject(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> T getObjWithCls(String key, Class<T> clazz) {

        Object result = redisTemplate.opsForValue().get(key);

        if (clazz.isInstance(result)) {
            return clazz.cast(result);
        }

        return null;
    }

    public Boolean containKey(String key) {
        return redisTemplate.hasKey(key);
    }

    //插入一条记录到Redis中的Hash中
    public void putOneToHash(String key, Object mapKey, Object mapValue) {
        stringRedisTemplate.opsForHash().put(key, mapKey, mapValue);
    }

    public void putAllToHash(String key, Map<Object, Object> valueMap) {
        stringRedisTemplate.opsForHash().putAll(key, valueMap);
    }

    public void putObjToHash(String key, Object object) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        stringRedisTemplate.opsForHash().putAll(key, RedisUtils.objectToMap(object));
    }

    //获取Redis中的Hash中的一条记录
    public String getOneFromHash(String key, Object mapKey) {
        return (String) stringRedisTemplate.opsForHash().get(key, mapKey);
    }

    public Map<Object, Object> getMapFromHash(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    //修改Redis的Hash中的一条记录
    public boolean updateOneToHash(String key, Object mapKey, Object mapValue) {

        if (containKey(key) == null) {
            return false;
        }

        stringRedisTemplate.opsForHash().put(key, mapKey, mapValue);

        return true;
    }

    public Long increase(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    public Long increase(String key, long num) {
        return stringRedisTemplate.opsForValue().increment(key, num);
    }

    public Boolean remove(String key) {
        return stringRedisTemplate.delete(key);
    }

    public Boolean getAndSet(String key, long time) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, "1", time, TimeUnit.SECONDS);
    }
}