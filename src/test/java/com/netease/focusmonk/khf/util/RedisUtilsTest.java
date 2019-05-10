package com.netease.focusmonk.khf.util;

import com.netease.focusmonk.model.RedisUserInfo;
import com.netease.focusmonk.service.RedisServiceImpl;
import com.netease.focusmonk.utils.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * @ClassName RedisUtilsTest
 * @Author konghaifeng
 * @Date 2019/5/8 11:37
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtilsTest {

    @Resource
    private RedisServiceImpl redisService;

    @Test
    public void testGenerateRedisKey() {
        System.out.println(RedisUtils.generateKey(new String[]{"12", "12"}));
    }

    @Test
    public void testPutObjToHash() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        RedisUserInfo redisUserInfo = new RedisUserInfo();
        redisUserInfo.setRestTime(100L);
        redisUserInfo.setStarTime(100L);
        redisUserInfo.setStartRestTime(100L);
        redisUserInfo.setState(1);
        redisUserInfo.setRoomId(1);
        redisService.putObjToHash("testPutObjToHash", redisUserInfo);
    }

    @Test
    public void testGet() {
        System.out.println(redisService.getOneFromHash("testPutObjToHash", "restTime"));
        System.out.println(redisService.getOneFromHash("testPutObjToHash", "starTime"));
        System.out.println(redisService.getOneFromHash("testPutObjToHash", "startRestTime"));
        System.out.println(redisService.getOneFromHash("testPutObjToHash", "state"));
        System.out.println(redisService.getOneFromHash("testPutObjToHash", "userRoomId"));
    }

}
