package com.netease.focusmonk.khf;

import com.netease.focusmonk.model.User;
import com.netease.focusmonk.service.RedisServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
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

    @Before
    public void testSet() {

    }

    @Test
    public void testGet() {

        HashMap<Object, User> map = new HashMap<>();

        User user1 = new User();
        user1.setUsername("001");

        User user2 = new User();
        user2.setUsername("002");

        User user3 = new User();
        user3.setUsername("003");

        map.put(1, user1);
        map.put(2, user2);
        map.put(3, user3);

        redisService.set("userMap", map);
        Map<String, User> userMap = (Map<String, User>) redisService.get("userMap");
        for (User user : userMap.values()) {
            System.out.println(user.getUsername());
        }
    }
}
