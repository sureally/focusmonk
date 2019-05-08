package com.netease.focusmonk.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
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

    //插入一条记录到Redis中的Hash中
    public void putOneToHash(String key, Object mapKey, Object mapValue) {
        redisTemplate.opsForHash().put(key, mapKey, mapValue);
    }

    public void putAllToHash(String key, Map<Object, Object> valueMap) {
        redisTemplate.opsForHash().putAll(key, valueMap);
    }

    public void putObjToHash(String key, Object object) throws IllegalAccessException {


        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue = field.get(object);
            putOneToHash(key, fieldName, fieldValue);
        }
    }

    //获取Redis中的Hash中的一条记录
    public Object getOneFromHash(String key, Object mapKey) {
        return redisTemplate.opsForHash().get(key, mapKey);
    }

    public Map<Object, Object> getMapFromHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    //修改Redis的Hash中的一条记录
    public boolean updateOneToHash(String key, Object mapKey, Object mapValue) {

        if (containKey(key) == null) {
            return false;
        }

        redisTemplate.opsForHash().put(key, mapKey, mapValue);

        return true;
    }

    public Integer addOneToInt(String key) {
        Integer value = get(key, Integer.class);
        set(key, ++value);
        return value;
    }

    public Long addOneToLong(String key) {
        Long value = get(key, Long.class);
        set(key, ++value);
        return value;
    }
}