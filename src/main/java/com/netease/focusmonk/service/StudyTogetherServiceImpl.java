package com.netease.focusmonk.service;

import com.alibaba.fastjson.JSONObject;
import com.netease.focusmonk.common.CommonConstant;
import com.netease.focusmonk.common.RedisConstant;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.exception.ParamException;
import com.netease.focusmonk.model.RoomTask;
import com.netease.focusmonk.model.Summary;
import com.netease.focusmonk.model.TaskDetail;
import com.netease.focusmonk.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

import static com.netease.focusmonk.common.CommonConstant.*;

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
    private RoomTaskServiceImpl roomTaseService;

    @Autowired
    private TaskDetailServiceImpl taskDetailService;

    @Autowired
    private SummaryServiceImpl summaryService;

    /**
     * 开始学习
     * @param jwt
     * @param roomId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void setValueForStart (String jwt, int roomId) throws Exception{
        String key = getUserinfoKey(jwt, roomId);

        Date now = new Date();
        setRedisHashValue(key, RedisConstant.START_TIME, now);
        setRedisHashValue(key, RedisConstant.START_REST_TIME, now);
        setRedisHashValue(key, RedisConstant.REST_TIME, 0);
        setRedisHashValue(key, RedisConstant.STATE, RedisConstant.STARTED_CODE);
    }

    /**
     * 暂停学习
     * @param jwt
     * @param roomId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void setValueForPause(String jwt, int roomId) throws Exception {
        String key = getUserinfoKey(jwt, roomId);

        Date now = new Date();
        setRedisHashValue(key, RedisConstant.START_REST_TIME, now);
        setRedisHashValue(key, RedisConstant.STATE, RedisConstant.STOPED_CODE);
    }

    /**
     * 恢复学习
     * @param jwt
     * @param roomId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void setValueForRestart (String jwt, int roomId) throws Exception{
        String key = getUserinfoKey(jwt, roomId);

        Date now = new Date();
        long lastRestTime = getLongFromRedisHashValue(key, RedisConstant.REST_TIME);
        long lastStartRestTime = getLongFromRedisHashValue(key, RedisConstant.START_REST_TIME);
        long restTime = lastRestTime + now.getTime() - lastStartRestTime;
        if (restTime < 0) {
            throw new GeneralException("多人学习，学习时长为负数");
        }
        if (restTime > Integer.MAX_VALUE) {
            throw new GeneralException("多人学习，学习时长为超过Int最大值");
        }
        setRedisHashValue(key, RedisConstant.REST_TIME, restTime);
        setRedisHashValue(key, RedisConstant.START_REST_TIME, now);
        setRedisHashValue(key, RedisConstant.STATE, RedisConstant.STARTED_CODE);
    }


    /** 结束学习
     * @param jwt
     * @param roomId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public TaskDetail setValueForFinish(String jwt, int roomId, TaskDetail taskDetail) throws Exception {
        String key = getUserinfoKey(jwt, roomId);
        int userId = Integer.valueOf(getUserId(jwt));

        Date now = new Date();
        long durationTime = 0;
        long startTime = getLongFromRedisHashValue(key, RedisConstant.START_TIME);
        long restTime = getLongFromRedisHashValue(key, RedisConstant.REST_TIME);
        long startRestTime = getLongFromRedisHashValue(key, RedisConstant.START_REST_TIME);

        if (Objects.equals(RedisConstant.STOPED_CODE, stringRedisTemplate.opsForHash().get(key, RedisConstant.STATE))) {
            // 结束学习时，用户状态为暂停学习
            durationTime = startRestTime - startTime - restTime;
        } else {
            // 结束学习时，用户状态为正则学习
            durationTime = now.getTime() - startTime - restTime;
        }
        if (durationTime < 0) {
            throw new GeneralException("多人学习，休息时长为负数");
        }
        if (durationTime > Integer.MAX_VALUE) {
            throw new GeneralException("多人学习，休息时长为超过Int最大值");
        }

        // 可以不用更新的redis的值，但是如果此Redis没有被成功删除，调用此接口可能会得到较大的学习数据
        setRedisHashValue(key, RedisConstant.START_TIME, now);
        setRedisHashValue(key, RedisConstant.START_REST_TIME, now);
        setRedisHashValue(key, RedisConstant.REST_TIME, 0);
        setRedisHashValue(key, RedisConstant.STATE, RedisConstant.STOPED_CODE);

        int bookSum = milli2Minute(durationTime) / TIME_OF_BOOK_GENERATION;
        // 保存房间学习
        saveRoomTask(userId, roomId, durationTime, bookSum);

        // 保存学习日常sammary
        int summaryId = saveSummary(userId, startTime, durationTime, bookSum);

        // 保存学习详情
        taskDetail.setSummaryId(summaryId);
        saveTaskDetail(taskDetail, userId, startTime, durationTime, bookSum);


        // TODO: 调用退出房间接口

        return taskDetail;
    }

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

    /** 拼接在redis中使用的key
     * @param jwt
     * @param roomId
     * @return
     * @throws Exception
     */
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

    // 从redis读取hashkey并转换为long
    private long getLongFromRedisHashValue(String key, String hashkey) throws GeneralException {
        checkInputForRedis(key, hashkey);
        String val = (String) stringRedisTemplate.opsForHash().get(key, hashkey);
        if (val == null) {
            throw new GeneralException("redis key: " + key + ", hashkey: " + hashkey  + "对应value读出为 null");
        }
        return Long.valueOf(val);
    }

    private void setRedisHashValue(String key, String hashkey, Object obj) throws Exception{
        checkInputForRedis(key, hashkey);
        if (obj instanceof String) {
            stringRedisTemplate.opsForHash().put(key, hashkey, obj);
        } else if (obj instanceof Number) {
            stringRedisTemplate.opsForHash().put(key, hashkey, String.valueOf(obj));
        } else if (obj instanceof Date) {
            setRedisHashValue(key, hashkey, ((Date) obj).getTime());
        } else {
            throw new ParamException("待插入redis hash中的value非String、非数字、非Date");
        }
    }

    // 保存roomtask
    private void saveRoomTask(int userId, int roomId, long durationTime, int bookSum) throws Exception {
        RoomTask roomTaskRecord = new RoomTask();
        roomTaskRecord.setUserId(userId);
        roomTaskRecord.setRoomId(roomId);
        // 这里存的是分钟
        roomTaskRecord.setDurationTime(milli2Minute(durationTime));
        roomTaskRecord.setBookNum(bookSum);
        roomTaseService.addOneRoomTask(roomTaskRecord);
    }

    // 保存summary, 返回summaryId
    private int saveSummary(int userId, long startTime, long durationTime, int bookSum) throws Exception {
        Summary oldSummary = summaryService.getSummaryByUserIdAndDay(new Date(startTime), userId);
        int summaryId = 0;
        // 需要保证数据库的字段值不能为null
        if (oldSummary != null) {
            // 数据库已有该日数据
            summaryService.accumulateBookAndTime(oldSummary.getId(), bookSum, (int) durationTime);
            summaryId = oldSummary.getId();
        } else {
            // 该日第一条数据，则新建
            Summary summary = new Summary();
            summary.setUserId(userId);
            summary.setSumBook(bookSum);
            summary.setSumTime((int) durationTime);
            summary.setSummaryDay(new Date(startTime));
            summaryId = summaryService.addSummary(summary);
        }
        return summaryId;
    }

    // 保存taskDetail
    private void saveTaskDetail(TaskDetail taskDetail, int userId, long startTime, long durationTime, int bookSum) throws Exception{
        taskDetail.setStartTime(new Date(startTime));
        taskDetail.setEndTime(new Date(startTime + durationTime));
        taskDetail.setUserId(userId);
        // 这里存的是分钟
        taskDetail.setDurationTime(milli2Minute(durationTime));
        taskDetail.setBookNum(bookSum);
        byte taskState = taskDetail.getTaskState();
        int planTime = taskDetail.getPlanTime();
        if (taskState != 0) {
            if (planTime < milli2Minute(durationTime)) {
                taskState = 1;
            } else {
                taskState = 2;
            }
        }
        taskDetail.setTaskState(taskState);
        taskDetailService.generateTaskDetail(taskDetail);
    }

    /**
     * 毫秒转换为分钟，超出 Integer.MAX_VALUE 抛出异常
     * @param milli
     * @return
     */
    private int milli2Minute(long milli) throws GeneralException {
        long mins = milli / MILLI_2_SECOND / SECOND_2_MINUTE;
        if (mins > Integer.MAX_VALUE) {
            throw new GeneralException("毫秒转换为分钟后数值大于 Integer.MAX_VALUE");
        }
        return (int) mins;
    }

    private void checkInputForRedis(String key, String hashkey) throws GeneralException {
        if (key == null) {
            throw new GeneralException("redis key: " + key + " 为 null");
        }
        if (hashkey == null) {
            throw new GeneralException("redis hashkey: " + hashkey + " 为 null");
        }
    }

}
