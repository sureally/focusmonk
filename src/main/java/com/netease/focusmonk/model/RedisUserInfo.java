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

    private Integer roomId;

    private Integer userId;

    private String nickName;

    private String image;

    //单位毫秒
    private Long starTime;

    //单位毫秒
    private Long startRestTime;

    //单位毫秒
    private Long restTime;

    //1-学习中，0-未学习
    private Integer state;
}