package com.netease.focusmonk.service;

import com.alibaba.fastjson.JSONObject;
import com.netease.focusmonk.exception.GeneralException;
import com.netease.focusmonk.exception.ParamException;
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

    private final String PREFIX_HOME = "home_";
    private final String PREFIX_USER = "_user_";

    private final String START_REST_TIME = "startRestTime";
    private final String START_TIME = "starTime";
    private final String REST_TIME = "restTime";
    private final String STATE = "state";
    private final String STARTED_CODE = "1";
    private final String STOPED_CODE = "0";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
        String key = PREFIX_HOME + homeId + PREFIX_USER + userId;
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
        stringRedisTemplate.opsForHash().put(key, START_TIME, now.getTime());
        stringRedisTemplate.opsForHash().put(key, START_REST_TIME, now.getTime());
        stringRedisTemplate.opsForHash().put(key, STATE, STARTED_CODE);
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
        stringRedisTemplate.opsForHash().put(key, START_REST_TIME, now.getTime());
        stringRedisTemplate.opsForHash().put(key, STATE, STOPED_CODE);
        return true;
    }

    /** 结束学习
     * @param jwt
     * @param homeId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public long setValueForFinish(String jwt, int homeId) throws Exception {
        String key = getUserinfoKey(jwt, homeId);

        long durationTime = getDurationTime(key);

        // 数据库持久化


        // TODO: 调用退出房间接口

        return durationTime;
    }

    private long getDurationTime (String key) throws Exception {
        Date now = new Date();
        long durationTime = 0L;
        long startTime = (long) stringRedisTemplate.opsForHash().get(key, START_TIME);
        long restTime = (long) stringRedisTemplate.opsForHash().get(key, REST_TIME);

        if (Objects.equals(STOPED_CODE, stringRedisTemplate.opsForHash().get(key, STATE))) {
            long startRestTime = (long) stringRedisTemplate.opsForHash().get(key, START_REST_TIME);
            durationTime = startRestTime - startTime - restTime;
        } else {
            durationTime = now.getTime() - startTime - restTime;
        }
        return durationTime;
    }


}
