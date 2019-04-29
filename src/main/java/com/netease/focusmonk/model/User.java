package com.netease.focusmonk.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
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
}