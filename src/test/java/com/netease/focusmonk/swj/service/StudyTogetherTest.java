package com.netease.focusmonk.swj.service;

import com.alibaba.fastjson.JSONObject;
import com.netease.focusmonk.common.CommonConstant;
import com.netease.focusmonk.common.RedisConstant;
import com.netease.focusmonk.model.TaskDetail;
import com.netease.focusmonk.service.StudyTogetherServiceImpl;
import com.netease.focusmonk.utils.JWTUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName com.netease.focusmonk.swj.service.StudyTogetherTest
 * @Desciption
 * @Author Shu WJ
 * @DateTime 2019-05-08 16:03
 * @Version 1.0
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudyTogetherTest {
    @Resource
    private StudyTogetherServiceImpl studyTogetherService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private void printObj(Object obj) {
        System.out.println(obj);
    }
    @Test
    public void getInRoomTest() throws Exception {
        int a = 0;
        printObj(a);
    }

    @Test
    public void otherTest() throws Exception {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJ1c2VySWRcIjozfSIsImp0aSI6ImQ1M2U5ZDU1LWVjZDktNDM4NC04ZmUwLWMyMGUxMmFiMGQ0YyIsImlhdCI6MTU1NzMwMzYwNiwiZXhwIjoxNTU3NTYyODA2fQ.ZOSAmC8wgs8kQ7D11g_32dBNLsL_He0NCMZNwK2o0Ng";

        Map<String, Object> map = new Hashtable<>();
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        int userId = Integer.valueOf(sessionInfo.getString("userId"));

        int roomId = 0;
        Date now = new Date();
        String key = RedisConstant.PREFIX_ROOM + CommonConstant.REDIS_KEY_SPLICING_SYMBOL + roomId +
                CommonConstant.REDIS_KEY_SPLICING_SYMBOL + RedisConstant.PREFIX_USER +
                CommonConstant.REDIS_KEY_SPLICING_SYMBOL + userId;

        map.put(RedisConstant.USER_ROOM_ID, String.valueOf(roomId));
        map.put(RedisConstant.START_TIME, String.valueOf(now.getTime()));
        map.put(RedisConstant.START_REST_TIME, String.valueOf(now.getTime()));
        map.put(RedisConstant.REST_TIME, String.valueOf(0));
        map.put(RedisConstant.STATE, String.valueOf(0));


        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setUserId(userId);
        taskDetail.setPlanTime(80);
        taskDetail.setTask("测试多人学习");
        taskDetail.setType(Byte.valueOf("1"));
        taskDetail.setTaskState(Byte.valueOf("1"));

        stringRedisTemplate.opsForHash().putAll(key, map);
        studyTogetherService.setValueForStart(jwt, 0);
        System.out.println("========= 开始学习 ==========");
        printAllUserinfoInRedis(key);
        Thread.sleep(30000);
        System.out.println("========= 持续 10000ms ==========");

        System.out.println("========= 暂停学习 ==========");
        studyTogetherService.setValueForPause(jwt, roomId);
        printAllUserinfoInRedis(key);
        Thread.sleep(30000);
        System.out.println("========= 持续 10000ms ==========");

        System.out.println("========= 恢复学习 ==========");
        studyTogetherService.setValueForRestart(jwt, roomId);
        printAllUserinfoInRedis(key);

        Thread.sleep(30000);
        System.out.println("========= 持续 10000ms ==========");
        System.out.println("========= 结束学习 ==========");
        studyTogetherService.setValueForFinish(jwt, roomId, taskDetail);
        printAllUserinfoInRedis(key);
    }

    private void printAllUserinfoInRedis(String key) {
        Set<String> set = new HashSet<>();
        set.add(RedisConstant.USER_ROOM_ID);
        set.add(RedisConstant.START_TIME);
        set.add(RedisConstant.START_REST_TIME);
        set.add(RedisConstant.REST_TIME);
        set.add(RedisConstant.STATE);
        for (String hashkey : set) {
            String val = (String) stringRedisTemplate.opsForHash().get(key, hashkey);
            System.out.println(hashkey + ": " + val);
        }
    }
}
