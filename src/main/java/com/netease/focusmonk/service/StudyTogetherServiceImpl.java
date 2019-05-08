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

    public String getUserId(String jwt) throws Exception{
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

    private String getUserinfoKey(String jwt, int roomId) throws Exception {
        String userId = getUserId(jwt);
        String key = RedisConstant.PREFIX_ROOM + CommonConstant.REDIS_KEY_SPLICING_SYMBOL + roomId +
                CommonConstant.REDIS_KEY_SPLICING_SYMBOL + RedisConstant.PREFIX_USER +
                CommonConstant.REDIS_KEY_SPLICING_SYMBOL + userId;
        if (!stringRedisTemplate.hasKey(key)) {
            throw new GeneralException("用户userId=" + getUserId(jwt) + " 不在房间roomId=" + roomId + "中，" +
                    "或则房间不存在");
        }
        return key;
    }

    /**
     * 开始学习
     * @param jwt
     * @param roomId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setValueForStart (String jwt, int roomId) throws Exception{
        String key = getUserinfoKey(jwt, roomId);

        Date now = new Date();
        stringRedisTemplate.opsForHash().put(key, RedisConstant.START_TIME, String.valueOf(now.getTime()));
        stringRedisTemplate.opsForHash().put(key, RedisConstant.START_REST_TIME, String.valueOf(now.getTime()));
        stringRedisTemplate.opsForHash().put(key, RedisConstant.STATE, RedisConstant.STARTED_CODE);

        // TODO：记录taskDetail

        return true;
    }

    /**
     * 暂停学习
     * @param jwt
     * @param roomId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setValueForPause(String jwt, int roomId) throws Exception {
        String key = getUserinfoKey(jwt, roomId);

        Date now = new Date();
        stringRedisTemplate.opsForHash().put(key, RedisConstant.START_REST_TIME, String.valueOf(now.getTime()));
        stringRedisTemplate.opsForHash().put(key, RedisConstant.STATE, RedisConstant.STOPED_CODE);
        return true;
    }

    /**
     * 恢复学习
     * @param jwt
     * @param roomId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setValueForRestart (String jwt, int roomId) throws Exception{
        String key = getUserinfoKey(jwt, roomId);

        Date now = new Date();
        String lastStartRestTime = (String) stringRedisTemplate.opsForHash().get(key, RedisConstant.START_REST_TIME);
        String restTime = (String) stringRedisTemplate.opsForHash().get(key, RedisConstant.REST_TIME);
        if (restTime == null ) {
            throw new GeneralException("RedisConstant.REST_TIME 是null");
        }
        if (lastStartRestTime == null) {
            throw new GeneralException("RedisConstant.START_REST_TIME 是null");
        }
        stringRedisTemplate.opsForHash().put(key,
                RedisConstant.REST_TIME,
                String.valueOf(
                        Long.valueOf(restTime) + now.getTime() - Long.valueOf(lastStartRestTime)));
        stringRedisTemplate.opsForHash().put(key, RedisConstant.START_REST_TIME, String.valueOf(now.getTime()));
        stringRedisTemplate.opsForHash().put(key, RedisConstant.STATE, RedisConstant.STARTED_CODE);

        return true;
    }


    /** 结束学习
     * @param jwt
     * @param roomId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public long setValueForFinish(String jwt, int roomId, TaskDetail taskDetail) throws Exception {
        String key = getUserinfoKey(jwt, roomId);

        Date now = new Date();
        long durationTime = 0;
        String startTimeStr = (String) stringRedisTemplate.opsForHash().get(key, RedisConstant.START_TIME);
        String restTimeStr = (String) stringRedisTemplate.opsForHash().get(key, RedisConstant.REST_TIME);
        String startRestTimeStr = (String) stringRedisTemplate.opsForHash().get(key, RedisConstant.START_REST_TIME);
        if (startRestTimeStr == null) {
            throw new GeneralException("RedisConstant.startRestTime 是null");
        }
        if (startTimeStr == null) {
            throw new GeneralException("RedisConstant.START_TIME 是null");
        }
        if (restTimeStr == null) {
            throw new GeneralException("RedisConstant.REST_TIME 是null");
        }
        long startTime = Long.valueOf(startTimeStr);
        long restTime = Long.valueOf(restTimeStr);
        long startRestTime = Long.valueOf(startRestTimeStr);

        if (Objects.equals(RedisConstant.STOPED_CODE, stringRedisTemplate.opsForHash().get(key, RedisConstant.STATE))) {

            durationTime = startRestTime - startTime - restTime;
        } else {
            durationTime = now.getTime() - startTime - restTime;
        }
        int bookSum = (int)(durationTime / 1000 / 60 / 20);
        // 保存roomtask
        RoomTask roomTaskRecord = new RoomTask();
        roomTaskRecord.setUserId(Integer.valueOf(getUserId(jwt)));
        roomTaskRecord.setRoomId(roomId);
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
