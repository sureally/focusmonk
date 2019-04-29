package com.netease.focusmonk.model;

import java.util.Date;

public class User {
    private Integer id;

    private String username;

    private String openId;

    private String phone;

    private String image;

    private Byte sex;

    private String lastDevice;

    private Date defaultTime;

    private Date defaultTask;

    private Integer sumBook;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getLastDevice() {
        return lastDevice;
    }

    public void setLastDevice(String lastDevice) {
        this.lastDevice = lastDevice == null ? null : lastDevice.trim();
    }

    public Date getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(Date defaultTime) {
        this.defaultTime = defaultTime;
    }

    public Date getDefaultTask() {
        return defaultTask;
    }

    public void setDefaultTask(Date defaultTask) {
        this.defaultTask = defaultTask;
    }

    public Integer getSumBook() {
        return sumBook;
    }

    public void setSumBook(Integer sumBook) {
        this.sumBook = sumBook;
    }
}