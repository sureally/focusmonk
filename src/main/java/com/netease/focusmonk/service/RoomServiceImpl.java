package com.netease.focusmonk.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hejiecheng
 * @Date 2019-05-08
 */

@Slf4j
@Service
public class RoomServiceImpl {

    private final RoomMapper roomMapper;

    private RedisServiceImpl redisService;

    @Autowired
    public RoomServiceImpl(RedisServiceImpl redisService, RoomMapper roomMapper) {
        this.redisService = redisService;
        this.roomMapper = roomMapper;
    }

    /**
     * 初始化房间信息
     * @param roomName
     * @param roomIntroduce
     * @param userId
     * @return
     */
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
        // 加入房间信息
        RoomRedis roomRedis = new RoomRedis();
        String roomKey = RedisUtils.generateKey(new String[]{RedisConstant.PREFIX_ROOM, String.valueOf(room.getId())});
        redisService.setObject(roomKey, roomRedis);
        // 初始化人数
        String roomPeopelKey = RedisUtils.generateKey(new String[]{
                RedisConstant.PREFIX_ROOM,
                String.valueOf(room.getId()),
                RedisConstant.SUFFIX_ROOM_PEOPLE_NUMBER
        });
        redisService.set(roomPeopelKey, "0");

        return room.getId();
    }

    /**
     * 获取某一页的房间信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Room> getRoomList(Integer pageNum, Integer pageSize) {
        String key = "room_list_" + pageNum + "_" + pageSize;
        String roomString = redisService.get(key);
        if (roomString == null) {
            PageHelper.startPage(pageNum, pageSize);
            List<Room> roomList = roomMapper.getRoomList();
            redisService.setWithTTL(key, JSON.toJSONString(roomList), 30);
            PageInfo<Room> roomPageInfo = new PageInfo<>(roomList);
            return roomPageInfo;
        } else {
            List<Room> roomList = new ArrayList<>();
            try {
                JSONArray roomArray = JSON.parseArray(roomString);
                for (int i = 0 ; i < roomArray.size(); ++i) {
                    Room room = JSON.parseObject(roomArray.getJSONObject(i).toJSONString(), Room.class);
                    roomList.add(room);
                }
            } catch (Exception e) {
                log.info("从redis中读取的房间列表信息解析错误，{}-{}", pageNum, pageSize);
                return null;
            }
            return new PageInfo<>(roomList);
        }
    }

    /**
     * 获取某个用户自己的房间
     * @param userId
     * @return
     */
    public List<Room> getUserRoomList(String userId) {
        List<Room> roomList = roomMapper.getUserRoomList(Integer.valueOf(userId));
        return roomList;
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

    @Transactional
    public JsonResult enterRoom(String userId, String roomId) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //获取房间人数Key
        String roomPeoNumKey = RedisUtils.generateKey(new String[]{RedisConstant.PREFIX_ROOM, roomId, RedisConstant.SUFFIX_ROOM_PEOPLE_NUMBER});

        if (!redisService.containKey(roomPeoNumKey)) {
            return JsonResult.getCustomResult(ResultCode.ROOM_NOT_EXIST_ERROR);
        }

        //获取房间人数
        int peopleNum = Integer.parseInt(redisService.get(roomPeoNumKey));

        if (peopleNum >= 12) {
            return JsonResult.getCustomResult(ResultCode.FULL_ROOM_ERROR);
        }

        String[] inRoomKeys = {RedisConstant.PREFIX_INROOM, userId};
        String inRoomKey = RedisUtils.generateKey(inRoomKeys);
        String isInRoom = redisService.get(inRoomKey);

        if (isInRoom != null) {
            return JsonResult.getCustomResult(ResultCode.USER_REPEATEDLY_ENTERS_ROOM_ERROR);
        }

        redisService.set(inRoomKey, RedisConstant.IN_ROOM_FLAG);

        redisService.increase(roomPeoNumKey);

        String[] roomInfoKeys = {RedisConstant.PREFIX_ROOM, roomId};
        String roomInfoKey = RedisUtils.generateKey(roomInfoKeys);

        RoomRedis roomRedis = redisService.getObjWithCls(roomInfoKey, RoomRedis.class);

        if (roomRedis == null) {
            return JsonResult.getCustomResult(ResultCode.ROOM_NOT_EXIST_ERROR);
        }

        roomRedis.getMemberList().add(userId);
        roomRedis.getNumAndIncr();

        redisService.setObject(roomInfoKey, roomRedis);

        RedisUserInfo userInfo = new RedisUserInfo();
        userInfo.setUserId(Integer.parseInt(userId));
        userInfo.setRoomId(Integer.parseInt(roomId));
        userInfo.setStarTime(new Date().getTime());
        userInfo.setState(1);

        String[] redisUserInfoKeys = {RedisConstant.PREFIX_ROOM, roomId, RedisConstant.PREFIX_USER, userId};
        String redisUserInfoKey = RedisUtils.generateKey(redisUserInfoKeys);

        redisService.putObjToHash(redisUserInfoKey, userInfo);

        return JsonResult.getCustomResult(ResultCode.SUCCESS);
    }

    @Transactional
    public JsonResult exitRoom(String userId, String roomId) {

        String roomInfoKey = RedisUtils.generateKey(new String[]{RedisConstant.PREFIX_ROOM, roomId});

        RoomRedis roomInfo = redisService.getObjWithCls(roomInfoKey, RoomRedis.class);

        if (!roomInfo.getMemberList().contains(userId)) {
            return JsonResult.getCustomResult(ResultCode.USER_EXITED_ROOM);
        }

        boolean isRemoved = roomInfo.getMemberList().remove(userId);
        System.out.println(isRemoved);

        String roomPeoNumKey = RedisUtils.generateKey(new String[]{RedisConstant.PREFIX_ROOM, roomId, RedisConstant.SUFFIX_ROOM_PEOPLE_NUMBER});
        redisService.increase(roomPeoNumKey, -1);

        String inRoomKey = RedisUtils.generateKey(new String[]{RedisConstant.PREFIX_INROOM, userId});
        redisService.remove(inRoomKey);

        String redisUserInfoKey = RedisUtils.generateKey(new String[]{RedisConstant.PREFIX_ROOM, roomId, RedisConstant.PREFIX_USER, userId});
        redisService.remove(redisUserInfoKey);

        return JsonResult.getSuccessResult();
    }
}
