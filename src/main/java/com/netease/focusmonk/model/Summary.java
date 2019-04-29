package com.netease.focusmonk.model;

import java.util.Date;

public class Summary {
    private Integer id;

    private Integer userId;

    private Integer sumTime;

    private Integer sumBook;

    private Date summaryDay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSumTime() {
        return sumTime;
    }

    public void setSumTime(Integer sumTime) {
        this.sumTime = sumTime;
    }

    public Integer getSumBook() {
        return sumBook;
    }

    public void setSumBook(Integer sumBook) {
        this.sumBook = sumBook;
    }

    public Date getSummaryDay() {
        return summaryDay;
    }

    public void setSummaryDay(Date summaryDay) {
        this.summaryDay = summaryDay;
    }
}