package com.netease.focusmonk.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Summary {
    private Integer id;

    private Integer userId;

    private Integer sumTime;

    private Integer sumBook;

    private Date summaryDay;

    private List<TaskDetail> taskDetailsList;

    @Transient
    private String context;
}