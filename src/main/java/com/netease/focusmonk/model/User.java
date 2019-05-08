package com.netease.focusmonk.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class User implements Serializable {
    private Integer id;

    private String username;

    private String openId;

    private String phone;

    private String image;

    private Byte sex;

    private String lastDevice;

    private Integer defaultTime;

    private String defaultTask;

    private Integer sumBook;

    private Integer sumTime;
}