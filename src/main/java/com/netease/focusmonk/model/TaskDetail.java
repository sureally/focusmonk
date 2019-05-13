package com.netease.focusmonk.model;

import com.netease.focusmonk.annotation.CheckDurationTime;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;

@Data
@GroupSequence({NotNull.List.class, Range.List.class, Length.List.class, TaskDetail.class})
@CheckDurationTime(groups = Default.class)
public class TaskDetail {

    private Integer id;

    private Integer userId;

    private Integer summaryId;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @NotNull(message = "开始学习时间不能为空", groups = NotNull.List.class)
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @NotNull(message = "结束学习时间不能为空", groups = NotNull.List.class)
    private Date endTime;

    //持续时长上线不超过1年
    @NotNull(message = "持续学习时长不能为空", groups = NotNull.List.class)
    @Range(min = 0, max = 31622400, message = "持续学时长存在异常", groups = Range.List.class)
    private Integer durationTime;

    @NotNull(message = "计划学习时长不能为空", groups = NotNull.List.class)
    @Range(min = 0, max = 7200,message = "计划学习时长有误", groups = Range.List.class)
    private Integer planTime;

    @Length(max = 64, message = "任务名字数个数范围：0~64", groups = Length.List.class)
    private String task;

    //0-未完成学习目标，1-完成目标
    @NotNull(message = "任务完成状态不能为空", groups = NotNull.List.class)
    @Range(min = 0, max = 1, message = "学习任务完成状态参数错误", groups = Range.List.class)
    private Byte taskState;

    @Range(min = 0, message = "卷数数目错误", groups = Range.List.class)
    private Integer bookNum;

    //0-单人学习，1-多人学习
    @NotNull(message = "学习类型不能为空", groups = NotNull.List.class)
    @Range(min = 0, max = 1, message = "学习类型参数错误", groups = Range.List.class)
    private Byte type;
}