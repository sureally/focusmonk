package com.netease.focusmonk.common;

/**
 * @InterfaceName com.netease.focusmonk.common.RedisConstant
 * @Desciption Redis数据库中使用的常量
 * @Author Shu WJ
 * @DateTime 2019-05-08 16:37
 * @Version 1.0
 **/
public interface RedisConstant {
    // redis key 的构建前缀
    String PREDIX_HOME = "room";
    String PREDIX_USER = "user";
    String PREDIX_INHOME = "inroom";

    // 用户信息valued对应的hashkey常量
    String USER_HOME_ID = "userRoomId";
    String START_TIME = "starTime";
    String START_REST_TIME = "startRestTime";
    String REST_TIME = "restTime";
    String STATE = "state";
    String STARTED_CODE = "1";
    String STOPED_CODE = "0";
}
