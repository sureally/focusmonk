package com.netease.focusmonk.service;

import com.netease.focusmonk.common.JsonResult;
import com.netease.focusmonk.common.RedisConstant;
import com.netease.focusmonk.common.ResultCode;
import com.netease.focusmonk.dao.RoomMapper;
import com.netease.focusmonk.model.RedisUserInfo;
import com.netease.focusmonk.model.Room;
import com.netease.focusmonk.model.RoomRedis;
import com.netease.focusmonk.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hejiecheng
 * @Date 2019-05-08
 */

@Slf4j
@Service
public class RoomServiceImpl {

    private final StringRedisTemplate redisTemplate;

    private final RoomMapper roomMapper;

    @Resource
    private RedisServiceImpl redisService;

    @Autowired
    public RoomServiceImpl(StringRedisTemplate stringRedisTemplate, RoomMapper roomMapper) {
        this.redisTemplate = stringRedisTemplate;
        this.roomMapper = roomMapper;
    }

    public int initRoom(String roomName, String roomIntroduce, String userId) {

        Room room = new Room();
        room.setName(roomName);
        room.setIntroduce(roomIntroduce);
        room.setOwner(Integer.valueOf(userId));
        // 先在出入数据库，获取id
        try {
            int result = roomMapper.insert(room);
            if (result != 1) {
                return -1;
            }
        } catch (Exception e) {
            log.info("创建房间失败：name-{}, introduce-{}, userId-{}", roomName, roomIntroduce, userId);
            throw e;
        }

        // 在redis中初始化
        return room.getId();
    }

    /**
     * 用户和房间进行解绑
     * @param roomId
     * @param userId
     * @return
     */
    public boolean untiedRoom(String roomId, String userId) {
        Room room = new Room();
        room.setId(Integer.valueOf(roomId));
        room.setOwner(Integer.valueOf(userId));
        return roomMapper.untiedRoom(room) == 1;
    }

    public JsonResult enterRoom(String userId, String roomId) throws IllegalAccessException {

        //获取房间人数Key
        String roomPeoNumKey = RedisUtils.generateKey(new String[]{RedisConstant.PREFIX_ROOM, roomId, RedisConstant.SUFFIX_ROOM_PEOPLE_NUMBER});

        if (!redisService.containKey(roomPeoNumKey)) {
            return JsonResult.getCustomResult(ResultCode.ROOM_NOT_EXIST_ERROR);
        }

        //获取房间人数
        Integer peopleNum = redisService.get(roomPeoNumKey, Integer.class);

        if (peopleNum >= 12) {
            return JsonResult.getCustomResult(ResultCode.FULL_ROOM_ERROR);
        }

        String[] inRoomKeys = {RedisConstant.PREFIX_INROOM, userId};
        String inRoomKey = RedisUtils.generateKey(inRoomKeys);
        Boolean isInRoom = redisService.get(inRoomKey, Boolean.class);

        if (isInRoom != null) {
            return JsonResult.getCustomResult(ResultCode.USER_REPEATEDLY_ENTERS_ROOM_ERROR);
        }

        redisService.set(inRoomKey, true);

        redisService.addOneToInt(roomPeoNumKey);

        String[] roomInfoKeys = {RedisConstant.PREFIX_ROOM, roomId};
        String roomInfoKey = RedisUtils.generateKey(roomInfoKeys);

        RoomRedis roomRedis = redisService.get(roomInfoKey, RoomRedis.class);

        roomRedis.getMemberList().add(userId);
        int roomUserId = roomRedis.getNumAndIncr();

        redisService.set(roomInfoKey, roomRedis);

        RedisUserInfo userInfo = new RedisUserInfo();
        userInfo.setUserRoomId(roomUserId);
        userInfo.setState(0);

        String[] redisUserInfoKeys = {RedisConstant.PREFIX_ROOM, roomId, RedisConstant.PREFIX_USER, userId};
        String redisUserInfoKey = RedisUtils.generateKey(redisUserInfoKeys);

        redisService.putObjToHash(redisUserInfoKey, userInfo);

        return JsonResult.getCustomResult(ResultCode.SUCCESS);
    }
}
