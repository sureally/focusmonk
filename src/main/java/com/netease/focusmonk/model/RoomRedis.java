package com.netease.focusmonk.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hejiecheng
 * @Date 2019-05-08
 */

@Data
public class RoomRedis implements Serializable {

    private static final long serialVersionUID = 6263963840352192513L;

    private List<String> memberList;
    private Integer number;

    public RoomRedis() {
        this.memberList = new ArrayList<>();
        number = 0;
    }

}
