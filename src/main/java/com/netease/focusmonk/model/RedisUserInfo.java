package com.netease.focusmonk.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RedisUserInfo
 * @Author konghaifeng
 * @Date 2019/5/8 20:35
 **/

@Data
public class RedisUserInfo implements Serializable {

    private int userRoomId;

    //单位毫秒
    private long starTime;

    //单位毫秒
    private long startRestTime;

    //单位毫秒
    private long restTime;

    //1-学习中，0-未学习
    private int state;
}