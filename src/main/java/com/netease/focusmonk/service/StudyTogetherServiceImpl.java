package com.netease.focusmonk.service;

import com.alibaba.fastjson.JSONObject;
import com.netease.focusmonk.common.CommonConstant;
import com.netease.focusmonk.common.RedisConstant;
import com.netease.focusmonk.dao.RoomTaskMapper;
import com.netease.focusmonk.dao.TaskDetailMapper;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.exception.ParamException;
import com.netease.focusmonk.model.RoomTask;
import com.netease.focusmonk.model.TaskDetail;
import com.netease.focusmonk.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * @ClassName com.netease.focusmonk.service.StudyTogetherServiceImpl
 * @Desciption 多人房间，学习功能
 * @Author Shu WJ
 * @DateTime 2019-05-08 14:33
 * @Version 1.0
 **/
@Slf4j
@Service
public class StudyTogetherServiceImpl {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RoomTaskMapper roomTaskMapper;

    @Autowired
    private TaskDetailMapper taskDetailMapper;

    public String getUserId(String jwt) throws ParamException{
        if (jwt == null) {
            throw new ParamException("参数错误：jwt 获取为空");
        }
        String jwtJson = JWTUtil.parseJWT(jwt).getBody().getSubject();
        JSONObject sessionInfo = JSONObject.parseObject(jwtJson);
        String userId = sessionInfo.getString("userId");
        if (userId == null) {
            throw new ParamException("参数错误：userId 获取为空");
        }
        return userId;
    }

    private String getUserinfoKey(String jwt, int homeId) throws ParamException, GeneralException {
        String userId = getUserId(jwt);
        String key = RedisConstant.PREDIX_HOME + CommonConstant.REDIS_KEY_SPLICING_SYMBOL + homeId +
                CommonConstant.REDIS_KEY_SPLICING_SYMBOL + RedisConstant.PREDIX_USER +
                CommonConstant.REDIS_KEY_SPLICING_SYMBOL + userId;
        if (!stringRedisTemplate.hasKey(key)) {
            throw new GeneralException("用户userId=" + getUserId(jwt) + " 不在房间homeId=" + homeId + "中，" +
                    "或则房间不存在");
        }
        return key;
    }

    /**
     * 开始学习
     * @param jwt
     * @param homeId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setValueForStart (String jwt, int homeId) throws Exception{
        String key = getUserinfoKey(jwt, homeId);

        Date now = new Date();
        stringRedisTemplate.opsForHash().put(key, RedisConstant.START_TIME, now.getTime());
        stringRedisTemplate.opsForHash().put(key, RedisConstant.START_REST_TIME, now.getTime());
        stringRedisTemplate.opsForHash().put(key, RedisConstant.STATE, RedisConstant.STARTED_CODE);

        // TODO：记录taskDetail

        return true;
    }

    /**
     * 暂停学习
     * @param jwt
     * @param homeId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setValueForPause(String jwt, int homeId) throws Exception {
        String key = getUserinfoKey(jwt, homeId);

        Date now = new Date();
        stringRedisTemplate.opsForHash().put(key, RedisConstant.START_REST_TIME, now.getTime());
        stringRedisTemplate.opsForHash().put(key, RedisConstant.STATE, RedisConstant.STOPED_CODE);
        return true;
    }

    /** 结束学习
     * @param jwt
     * @param homeId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public long setValueForFinish(String jwt, int homeId, TaskDetail taskDetail) throws Exception {
        String key = getUserinfoKey(jwt, homeId);

        Date now = new Date();
        long durationTime = 0;
        long startTime = (long) stringRedisTemplate.opsForHash().get(key, RedisConstant.START_TIME);
        long restTime = (long) stringRedisTemplate.opsForHash().get(key, RedisConstant.REST_TIME);

        if (Objects.equals(RedisConstant.STOPED_CODE, stringRedisTemplate.opsForHash().get(key, RedisConstant.STATE))) {
            int startRestTime = (int) stringRedisTemplate.opsForHash().get(key, RedisConstant.START_REST_TIME);
            durationTime = startRestTime - startTime - restTime;
        } else {
            durationTime = now.getTime() - startTime - restTime;
        }
        int bookSum = (int)(durationTime / 1000 / 60 / 20);
        // 保存roomtask
        RoomTask roomTaskRecord = new RoomTask();
        roomTaskRecord.setUserId(Integer.valueOf(getUserId(jwt)));
        roomTaskRecord.setHomeId(homeId);
        // 这里存的是分钟
        roomTaskRecord.setDurationTime((int)(durationTime / 1000 / 60));
        roomTaskRecord.setBookNum(bookSum);
        roomTaskMapper.insert(roomTaskRecord);

        // 保存taskDetail
        taskDetail.setStartTime(new Date(startTime));
        taskDetail.setEndTime(new Date(startTime + durationTime));
        // 这里存的是分钟
        taskDetail.setDurationTime((int)(durationTime / 1000 / 60));
        taskDetail.setBookNum(bookSum);
        byte taskState = taskDetail.getTaskState();

        int planTime = taskDetail.getPlanTime();
        if (taskState != 0) {
            if (planTime < (int)(durationTime / 1000 / 60)) {
                taskState = 1;
            } else {
                taskState = 2;
            }
        }
        taskDetail.setTaskState(taskState);
        taskDetailMapper.insert(taskDetail);

        // TODO: 调用退出房间接口

        return durationTime;
    }



}
