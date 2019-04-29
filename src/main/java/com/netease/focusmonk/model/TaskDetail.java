package com.netease.focusmonk.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskDetail {
    private Integer id;

    private Integer userId;

    private Integer summaryId;

    private Date startTime;

    private Date endTime;

    private Integer durationTime;

    private Integer planTime;

    private String task;

    private Byte taskState;

    private Integer bookNum;

    private Byte type;
}