package com.netease.focusmonk.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class TaskDetail {
    private Integer id;

    private Integer userId;

    private Integer summaryId;

    @NotNull(message = "独自学习开始时间不能为空")
    @Past(message = "开始学习时间有误")
    private Date startTime;

    @NotNull(message = "独自学习结束时间不能为空")
    @Past(message = "结束学习时间有误")
    private Date endTime;

    @Size(max = 86400000, message = "学习时长有误")
    private Integer durationTime;

    @NotNull(message = "独自学习计划时间不能为空")
    private Integer planTime;

    @Length(max = 128, message = "任务名字数个数范围：0~64")
    private String task;

    private Byte taskState;

    @Size(max = 58, message = "卷数数目错误")
    private Integer bookNum;

    @Range(min = 0, max = 1, message = "学习类型参数错误")
    private Byte type;
}