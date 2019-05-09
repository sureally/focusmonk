package com.netease.focusmonk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
public class TaskDetail {
    private Integer id;

    private Integer userId;

    private Integer summaryId;

    @NotNull(message = "开始学习时间不能为空")
    private Date startTime;

    @NotNull(message = "结束学习时间不能为空")
    private Date endTime;

    @NotNull(message = "持续学习时长不能为空")
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