package com.netease.focusmonk.common;

public interface CommonConstant {

    //每本经书产生时长20分钟
    int TIME_OF_BOOK_GENERATION = 20;

    //Redis Key 拼接符
    String REDIS_KEY_SPLICING_SYMBOL = "_";

    // 毫秒与秒的转换值
    int MILLI_2_SECOND = 1000;

    // 秒与分钟的转换值
    int SECOND_2_MINUTE = 60;

    double SPEED_VOLUME_ERROR = 0.0001;
    double SPEED_VOLUME_MAX = 1.0;
    double SPEED_VOLUME_MIN = 0.0;
}
