package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.Room;

public interface RoomMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);

    int untiedRoom(Room room);
}