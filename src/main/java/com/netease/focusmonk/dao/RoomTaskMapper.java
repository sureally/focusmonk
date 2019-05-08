package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.RoomTask;

public interface RoomTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoomTask record);

    int insertSelective(RoomTask record);

    RoomTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoomTask record);

    int updateByPrimaryKey(RoomTask record);
}