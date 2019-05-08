package com.netease.focusmonk.khf.util;

import com.netease.focusmonk.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName RedisUtilsTest
 * @Author konghaifeng
 * @Date 2019/5/8 11:37
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisUtilsTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void testGenerateRedisKey() {

        System.out.println(redisUtil.generateKey(new String[]{"12", "12"}));
    }

}
