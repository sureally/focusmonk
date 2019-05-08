package com.netease.focusmonk.service;

import com.netease.focusmonk.dao.RoomMapper;
import com.netease.focusmonk.model.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author hejiecheng
 * @Date 2019-05-08
 */

@Slf4j
@Service
public class RoomServiceImpl {

    private final StringRedisTemplate redisTemplate;

    private final RoomMapper roomMapper;

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

}
