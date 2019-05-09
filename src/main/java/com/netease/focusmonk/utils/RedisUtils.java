package com.netease.focusmonk.utils;

import com.netease.focusmonk.common.CommonConstant;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author hejiecheng
 * @Date 2019-04-30
 */
public final class RedisUtils {

    //自动按下划线拼接RedisKey
    public static String generateKey(String[] keys) {

        if (keys == null) return null;

        if (keys.length <= 1) return keys[0];

        StringBuilder redisKey = new StringBuilder();
        for (String key : keys) {
            redisKey.append(CommonConstant.REDIS_KEY_SPLICING_SYMBOL).append(key);
        }

        return redisKey.substring(1);
    }

    //对象转为Map
    public static Map<String, String> objectToMap(Object obj) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(obj == null) return null;
        return BeanUtils.describe(obj);
    }

    //Map转为对象
    public static <T> T  mapToObject(Class<T> clazz, Map<String, String> map) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        Object destObj = clazz.newInstance();

        BeanUtils.populate(destObj, map);

        if (clazz.isInstance(destObj)) {
            return clazz.cast(destObj);
        }

        return null;
    }
}