package com.netease.focusmonk.dao;

import com.netease.focusmonk.model.Room;

import java.util.List;

public interface RoomMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Room record);

    Room selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);

    int untiedRoom(Room room);

    List<Room> getRoomList();

    List<Room> getUserRoomList(Integer userId);
}