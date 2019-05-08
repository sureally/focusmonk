package com.netease.focusmonk.utils;

import com.netease.focusmonk.common.CommonConstant;

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
}