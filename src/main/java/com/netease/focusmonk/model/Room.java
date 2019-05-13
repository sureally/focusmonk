package com.netease.focusmonk.model;

import lombok.Data;

@Data
public class Room {
    private Integer id;

    private String name;

    private String introduce;

    private Integer owner;

    private Integer state;

    private Integer userCount;
}