package com.netease.focusmonk.common;

/**
 * @InterfaceName com.netease.focusmonk.common.RedisConstant
 * @Desciption Redis数据库中使用的常量
 * @Author Shu WJ
 * @DateTime 2019-05-08 16:37
 * @Version 1.0
 **/
public interface RedisConstant {
    /**
     *  使用下面的常量拼接相应的 redis key。
     *  用户信息 key: PREDIX_ROOM + "_" + roomId + "_" + PREDIX_USER + "_" + userId
     *  房间信息 key: PREDIX_ROOM + "_" + roomId
     *  用户是否在房间内: PREDIX_INROOM + "_" + roomId
     */
    String PREDIX_ROOM = "room";
    String PREDIX_USER = "user";
    String PREDIX_INROOM = "inroom";

    // 用户信息 value 对应的 hashkey 常量
    String USER_ROOM_ID = "userRoomId";
    String START_TIME = "starTime";
    String START_REST_TIME = "startRestTime";
    String REST_TIME = "restTime";
    String STATE = "state";
    String STARTED_CODE = "1";
    String STOPED_CODE = "0";
}
