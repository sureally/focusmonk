package com.netease.focusmonk.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用于多人学习房间，和TaskDetail是一样的，只是在controller获取的时候校验的参数不一样
 * by shu wj
 */
@Getter
@Setter
public class RedisTaskDetailAO {
    private Integer id;

    private Integer userId;

    private Integer summaryId;

    private Date startTime;

    private Date endTime;

    private Integer durationTime;

    @NotNull(message = "计划学习时长不能为空")
    @Range(min = 0, message = "计划学习时长有误")
    private Integer planTime;

    @Length(max = 64, message = "任务名字数个数范围：0~64")
    private String task;

    //0-未完成学习目标，1-完成目标-时间学习时间小于计划时间，2-实际学习时间大于等于计划学习时间
    @NotNull
    @Range(min = 0, max = 2, message = "学习任务完成状态参数错误")
    private Byte taskState;

    @Range(min = 0, message = "卷数数目错误")
    private Integer bookNum;

    //0-单人学习，1-多人学习
    @NotNull
    @Range(min = 0, max = 1, message = "学习类型参数错误")
    private Byte type;
}