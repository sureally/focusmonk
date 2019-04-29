package com.netease.focusmonk.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Summary {
    private Integer id;

    private Integer userId;

    private Integer sumTime;

    private Integer sumBook;

    private Date summaryDay;
}