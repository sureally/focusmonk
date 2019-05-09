package com.netease.focusmonk.khf.service;

import com.netease.focusmonk.common.RedisConstant;
import com.netease.focusmonk.model.RedisUserInfo;
import com.netease.focusmonk.model.RoomRedis;
import com.netease.focusmonk.service.RedisServiceImpl;
import com.netease.focusmonk.service.RoomServiceImpl;
import com.netease.focusmonk.utils.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @ClassName RoomServiceTest
 * @Author konghaifeng
 * @Date 2019/5/8 21:56
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoomServiceTest {

    @Resource
    private RoomServiceImpl roomService;

    @Resource
    private RedisServiceImpl redisService;

    @Test
    public void testEnterRoom() {

        RoomRedis roomRedis = new RoomRedis();

        String roomPeoNumKey = RedisUtils.generateKey(new String[]{RedisConstant.PREFIX_ROOM, "roomIdKong001", RedisConstant.SUFFIX_ROOM_PEOPLE_NUMBER});
        String roomInfoKey = RedisUtils.generateKey(new String[]{RedisConstant.PREFIX_ROOM, "roomIdKong001"});

        redisService.set(roomPeoNumKey, "1");
        redisService.setObject(roomInfoKey, roomRedis);

        try {
            roomService.enterRoom("userIdKong001", "roomIdKong001");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGet() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        RedisUserInfo redisUserInfo = new RedisUserInfo();
        redisUserInfo.setUserRoomId(123);
        redisUserInfo.setStarTime(123L);

        redisService.putObjToHash("konghaifeng", redisUserInfo);

        Map<Object, Object> map = redisService.getMapFromHash("konghaifeng");

        for (Object value : map.values()) {
            System.out.println(value);
        }
    }
}
