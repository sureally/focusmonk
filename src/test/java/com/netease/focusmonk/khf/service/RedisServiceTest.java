package com.netease.focusmonk.khf.service;

import com.netease.focusmonk.common.RedisConstant;
import com.netease.focusmonk.model.RedisUserInfo;
import com.netease.focusmonk.service.RedisServiceImpl;
import com.netease.focusmonk.utils.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName RedisServiceTest
 * @Author konghaifeng
 * @Date 2019/5/8 15:45
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {

    @Resource
    private RedisServiceImpl redisService;

    @Test
    public void testSet() {
        redisService.set("testKey", "1");
    }

    @Test
    public void testGet() {
        String[] redisUserInfoKeys = {RedisConstant.PREFIX_ROOM, "roomIdKong001", RedisConstant.PREFIX_USER, "userIdKong001"};
        String redisUserInfoKey = RedisUtils.generateKey(redisUserInfoKeys);
        Map<Object, Object> mapFromHash = redisService.getMapFromHash(redisUserInfoKey);
        for (Object value : mapFromHash.values()) {
            System.out.println(value);
        }
    }

    @Test
    public void testContain() {
        System.out.println(redisService.containKey("testKey"));
    }

    @Test
    public void testMapToObj() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        RedisUserInfo redisUserInfo = new RedisUserInfo();
        redisUserInfo.setStarTime(new Date().getTime());

        redisService.putObjToHash("userInfoObj", redisUserInfo);
        Map<Object, Object> userInfoObj = redisService.getMapFromHash("userInfoObj");
        for (Object value : userInfoObj.values()) {
            System.out.println(value);
        }

    }
}